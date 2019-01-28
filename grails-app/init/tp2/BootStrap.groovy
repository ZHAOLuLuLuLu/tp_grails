package tp2

import fr.mbds.tp.Role
import fr.mbds.tp.User
import fr.mbds.tp.UserMessage
import fr.mbds.tp.Message
import fr.mbds.tp.UserRole


class BootStrap {

    def init = { servletContext ->
        def userLu = new User (username: "lu",password: "secret", firstName: "lu", lastName: "lu", email: "mailLu").save(flush: true, failOnError: true)
        def roleLu = new Role (authority:"ROLE_LU").save(flush: true, failOnError: true)
        UserRole.create(userLu,roleLu,true)
        (1..50).each{
            def userInstance =  new User(username:"username-$it", password:"passeword", firstName: "first", lastName:"last", email: "mail-$it").save(flush:true, failOnError:true)
            new Message(messageContent: "lala", author:userInstance).save(flush:true, failOnError:true)
        }
        Message.list().each {
            Message messageInstance ->

                User.list().each {

                    User userInstance ->

                        UserMessage.create(userInstance, messageInstance, true)
                }
        }



    }
    def destroy = {
    }
}
