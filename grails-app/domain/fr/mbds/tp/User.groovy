package fr.mbds.tp

import groovy.transform.EqualsAndHashCode
import groovy.transform.ToString
import grails.compiler.GrailsCompileStatic

@GrailsCompileStatic
@EqualsAndHashCode(includes='username')
@ToString(includes='username', includeNames=true, includePackage=false)
class User implements Serializable {

    private static final long serialVersionUID = 1

    String username
    String password

    String firstName
    String lastName
    String email
    String tel
    Date dob

    Date dateCreated
    Date lastUpdated

    boolean enabled = true
    boolean accountExpired
    boolean accountLocked
    boolean passwordExpired

    Set<Role> getAuthorities() {
        (UserRole.findAllByUser(this) as List<UserRole>)*.role as Set<Role>
    }



    static constraints = {
        password nullable: false, blank: false, password: true
        username nullable: false, blank: false, unique: true
        email nullable: false, blank: false, unique: true
        tel nullable: true
        dob nullable: true
        lastName nullable: false, blank: false
        firstName nullable: false, blank: false

    }

    static mapping = {
	    password column: '`password`'
    }
}
