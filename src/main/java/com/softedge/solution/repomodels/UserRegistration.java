package com.softedge.solution.repomodels;

import com.softedge.solution.security.models.Authorities;

import javax.persistence.*;
import java.sql.Timestamp;
import java.util.Date;
import java.util.List;
import java.util.Objects;

@Entity
@Table(name="user_tbl")
public class UserRegistration {
	
	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private Long id;

	@Column(name="email_id")
	private String username;
    private String password;
    private boolean enabled;

    private String name;

    private String category;

    private Timestamp dob;
    private String gender;

    @Column(name = "nationality")
    private String nationality;

    private String photo;
    private Date createdDate;
    private String createdBy;
    private Date modifiedDate;
    private String modifiedBy;

    @OneToMany(cascade = CascadeType.ALL)
    private List<Authorities> authorities;

	@Column(name = "force_password_change")
	private boolean forcePasswordChange;
	@Column(name = "profile_completed")
	private boolean profileCompleted;
    @Column(name = "ipv_completed")
    private boolean ipvCompleted;

    private Long phone;

    @OneToOne(fetch = FetchType.LAZY,
            cascade = CascadeType.ALL,
            mappedBy = "user")
    private DigitalIPV digitalIPV;

    @OneToMany(fetch = FetchType.LAZY,
            cascade = CascadeType.ALL,
            mappedBy = "user")
    private List<Location> location;


    public UserRegistration() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
		this.id = id;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public boolean isEnabled() {
		return enabled;
	}

	public void setEnabled(boolean enabled) {
		this.enabled = enabled;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getCategory() {
		return category;
	}

	public void setCategory(String category) {
		this.category = category;
	}

	public Timestamp getDob() {
		return dob;
	}

	public void setDob(Timestamp dob) {
		this.dob = dob;
	}

	public List<Location> getLocation() {
		return location;
	}

    public void setLocation(List<Location> location) {
        this.location = location;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getNationality() {
        return nationality;
    }

    public void setNationality(String nationality) {
        this.nationality = nationality;
    }

    public String getPhoto() {
        return photo;
    }

    public void setPhoto(String photo) {
        this.photo = photo;
    }

	public Date getCreatedDate() {
		return createdDate;
	}

	public void setCreatedDate(Date createdDate) {
		this.createdDate = createdDate;
	}

	public String getCreatedBy() {
		return createdBy;
	}

	public void setCreatedBy(String createdBy) {
		this.createdBy = createdBy;
	}

	public Date getModifiedDate() {
		return modifiedDate;
	}

	public void setModifiedDate(Date modifiedDate) {
		this.modifiedDate = modifiedDate;
	}

	public String getModifiedBy() {
		return modifiedBy;
	}

	public void setModifiedBy(String modifiedBy) {
		this.modifiedBy = modifiedBy;
	}

	public List<Authorities> getAuthorities() {
		return authorities;
	}

	public void setAuthorities(List<Authorities> authorities) {
		this.authorities = authorities;
	}

	public boolean isForcePasswordChange() {
		return forcePasswordChange;
	}

	public void setForcePasswordChange(boolean forcePasswordChange) {
		this.forcePasswordChange = forcePasswordChange;
	}

	public boolean isProfileCompleted() {
		return profileCompleted;
	}

	public void setProfileCompleted(boolean profileCompleted) {
		this.profileCompleted = profileCompleted;
	}

	public boolean isIpvCompleted() {
		return ipvCompleted;
	}

	public void setIpvCompleted(boolean ipvCompleted) {
		this.ipvCompleted = ipvCompleted;
	}

	public Long getPhone() {
		return phone;
	}

	public void setPhone(Long phone) {
		this.phone = phone;
	}

	public DigitalIPV getDigitalIPV() {
		return digitalIPV;
	}

	public void setDigitalIPV(DigitalIPV digitalIPV) {
		this.digitalIPV = digitalIPV;
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (!(o instanceof UserRegistration)) return false;
		UserRegistration that = (UserRegistration) o;
		return isEnabled() == that.isEnabled() &&
                Objects.equals(getId(), that.getId()) &&
                Objects.equals(getUsername(), that.getUsername()) &&
                Objects.equals(getPassword(), that.getPassword()) &&
                Objects.equals(getName(), that.getName()) &&
                Objects.equals(getCategory(), that.getCategory()) &&
                Objects.equals(getDob(), that.getDob()) &&
                Objects.equals(getGender(), that.getGender()) &&
                Objects.equals(getNationality(), that.getNationality()) &&
                Objects.equals(getPhoto(), that.getPhoto()) &&
                Objects.equals(getCreatedDate(), that.getCreatedDate()) &&
                Objects.equals(getCreatedBy(), that.getCreatedBy()) &&
                Objects.equals(getModifiedDate(), that.getModifiedDate()) &&
                Objects.equals(getModifiedBy(), that.getModifiedBy()) &&
                Objects.equals(getAuthorities(), that.getAuthorities());
	}

	@Override
	public int hashCode() {
        return Objects.hash(getId(), getUsername(), getPassword(), isEnabled(), getName(), getCategory(), getDob(), getGender(), getNationality(), getPhoto(), getCreatedDate(), getCreatedBy(), getModifiedDate(), getModifiedBy(), getAuthorities());
    }

	@Override
	public String toString() {
		return "UserRegistration{" +
                "id=" + id +
                ", username='" + username + '\'' +
                ", password='" + password + '\'' +
                ", enabled=" + enabled +
                ", name='" + name + '\'' +
                ", category=" + category +
                ", dob='" + dob + '\'' +
                ", gender='" + gender + '\'' +
                ", nationality=" + nationality +
                ", photo='" + photo + '\'' +
                ", createdDate=" + createdDate +
                ", createdBy='" + createdBy + '\'' +
                ", modifiedDate=" + modifiedDate +
                ", modifiedBy='" + modifiedBy + '\'' +
                ", authorities=" + authorities +
                '}';
	}
}
