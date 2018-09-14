package mgt.inventory.pharmacy.entities;

import org.mongodb.morphia.annotations.Entity;
import org.mongodb.morphia.annotations.Field;
import org.mongodb.morphia.annotations.Index;
import org.mongodb.morphia.annotations.Indexes;

@Entity(value = "employees", noClassnameStored = true)
@Indexes(@Index(fields = { @Field(value = "employeeId") }))
public class Employee extends BaseEntity {

	private static final long serialVersionUID = 1L;

	private String employeeId;
	private String fullName;
	private String phoneNo;
	private String email;
	private String address;
	private String remark;

	@Override
	public boolean equals(Object obj) {
		if (obj instanceof Employee) {
			return employeeId.equals(((Employee) obj).getEmployeeId());
		} else
			return false;
	}

	public String getEmployeeId() {
		return employeeId;
	}

	public void setEmployeeId(String employeeId) {
		this.employeeId = employeeId;
	}

	public String getFullName() {
		return fullName;
	}

	public void setFullName(String fullName) {
		this.fullName = fullName;
	}

	public String getPhoneNo() {
		return phoneNo;
	}

	public void setPhoneNo(String phoneNo) {
		this.phoneNo = phoneNo;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}

	@Override
	public String toString() {
		return "Employee{" + "employeeId='" + employeeId + '\'' + ", fullName='" + fullName + '\'' + ", phoneNo='"
				+ phoneNo + '\'' + ", email='" + email + '\'' + ", address='" + address + '\'' + ", remark='" + remark
				+ '\'' + '}';
	}

	public Employee() {

	}

}
