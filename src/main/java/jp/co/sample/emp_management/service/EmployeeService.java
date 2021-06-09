package jp.co.sample.emp_management.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import jp.co.sample.emp_management.domain.Employee;
import jp.co.sample.emp_management.repository.EmployeeRepository;
import net.arnx.jsonic.JSON;

/**
 * 従業員情報を操作するサービス.
 * 
 * @author igamasayuki
 *
 */
@Service
@Transactional
public class EmployeeService {

	@Autowired
	private EmployeeRepository employeeRepository;
	
	/**
	 * 従業員情報を全件取得します.
	 * 
	 * @return　従業員情報一覧
	 */
	public List<Employee> showList(String searchName) {
		if(searchName == null){
			return employeeRepository.findAll();
		}else{
			return employeeRepository.ambiguousFind(searchName);
		}
	}
	
	/**
	 * 従業員情報を取得します.
	 * 
	 * @param id ID
	 * @return 従業員情報
	 * @throws org.springframework.dao.DataAccessException 検索されない場合は例外が発生します
	 */
	public Employee showDetail(Integer id) {
		Employee employee = employeeRepository.load(id);
		return employee;
	}
	
	/**
	 * 従業員情報を更新します.
	 * 
	 * @param employee 更新した従業員情報
	 */
	public void update(Employee employee) {
		employeeRepository.update(employee);
	}

	public Page<Employee> showPage(int page, int size, List<Employee> employeeList){
		int fromIndex = (page - 1) * size;
		int toIndex = Math.min(employeeList.size(), fromIndex + size);
		employeeList = employeeList.subList(fromIndex, toIndex);
		Pageable pageable = PageRequest.of(page, size);
		return new PageImpl<Employee>(employeeList, pageable, employeeList.size());
	}
	/**
	 * オートコンプリート用に従業員の名前一覧を作る．
	 * 
	 * @return JSON形式の名前リスト
	 */
	public String findAllForAutoComplete(){
		List<String> nameList = new ArrayList<>();
		for (Employee employee : employeeRepository.findAll()) {
			nameList.add(employee.getName());
		}
		return JSON.encode(nameList);
	}
}
