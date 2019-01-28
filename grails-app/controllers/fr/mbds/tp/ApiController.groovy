package fr.mbds.tp

import grails.converters.JSON
import grails.converters.XML

import javax.servlet.http.HttpServletRequest

class ApiController {

    def index() {render "ok" }
    def message () {
        switch (request.getMethod()) {
            case "GET":
                if (params.id) {
                    def messageInstance = Message.get(params.id)
                    if (messageInstance)
                        reponseFormat(messageInstance, request)
                    else
                        response.status = 404

                } else
                    forward action: "messages"
                break
            case "POST":
                forward action: "messages"
                break
            case "PUT":
                def messageInstance = params.id ? Message.get(params.id) : null
                if (messageInstance)
                {
                    if (params.get("author.id"))
                    {
                        def authorInstance = User.get((Long)params.get("author.id"))
                        if (authorInstance)
                            messageInstance.author = authorInstance
                    }
                    if (params.messageContent)
                        messageInstance.messageContent = params.messageContent
                    if (messageInstance.save(flush: true))
                        render (text: "Réussi $messageInstance.id")
                    else
                        render(status: 400,text: "Echec de la mise a jour du message $messageInstance.id")
                }
                else
                    render(status: 404,text: "La message désigné est introuvé")
                break
            case "DELETE":
                def messageInstance = params.id ? Message.get(params.id) : null
                if (messageInstance) {
                    def userMessages = UserMessage.findAllByMessage(messageInstance)
                    userMessages.each {
                        UserMessage userMessage ->
                            userMessage.delete(flush: true)
                    }
                    messageInstance.delete(flush: true)
                    render(status: 200, text: "message effacé")
                } else
                    render(status: 404, text: "message introuver")
                break
            default:
                response.status = 405
                break

        }
    }
    def messages()
    {
       switch (request.getMethod())
       {
           case "GET":
               reponseFormatList (Message.list(), request)
               break
           case "POST":
                //verifier auteur
           def authorInstance = params.author.id ? User.get(params.author.id) : null
           def messageInstance
           if (authorInstance)
           {

               //créer message
               messageInstance = new Message(author: authorInstance, messageContent: params.messageContent)
               if (messageInstance.save(flush: true))

               {//ajouter destinaires
                   if (params.receiver.id)
                   {
                       def receiverInstance = User.get(params.receiver.id)
                       i f(receiverInstance)
                           new UserMessage(user: receiverInstance, message: messageInstance).save(flush: true)
                   }
                   render (status: 201)
                   }
           }

               if (response.status != 201)
                   response.status = 400



               break


       }
    }
    def reponseFormat (Object instance, HttpServletRequest request) {
        switch (request.getHeader("Accept")) {
            case "text/xml":
                render instance as XML
                break
            case "text/json":
                render instance as JSON
                break
            default:
                response.status = 415
                break
        }
    }
    def reponseFormatList (List list, HttpServletRequest request) {
        switch (request.getHeader("Accept")) {
            case "text/xml":
                render list as XML
                break
            case "text/json":
                render list as JSON
                break
        }
    }

    def user () {
        switch (request.getMethod()) {
            case "GET":
                if (params.id) {
                    def userInstance = User.get(params.id)
                    if (userInstance)
                        reponseFormat(userInstance, request)
                    else
                        response.status = 404

                } else
                    forward action: "users"
                break
            case "POST":
                forward action: "users"
                break
            case "PUT":
                def userInstance = params.id ? User.get(params.id) : null
                if (userInstance)
                {
                    if (params.get("username.id"))
                    {
                        def usernameInstance = User.get((Long)params.get("username.id"))
                        if (usernameInstance)
                            usernameInstance.username = usernameInstance
                    }
                    if (params.get("password.id"))
                    {
                        def passwordInstance = User.get((Long)params.get("password.id"))
                        if (passwordInstance)
                            passwordInstance.password = passwordInstance
                    }
                    if (params.get("firstName.id"))
                    {
                        def firstNameInstance = User.get((Long)params.get("firstName.id"))
                        if (firstNameInstance)
                            firstNameInstance.firstName = firstNameInstance
                    }
                    if (params.get("lastName.id"))
                    {
                        def lastNameInstance = User.get((Long)params.get("lastName.id"))
                        if (lastNameInstance)
                            lastNameInstance.lastName = lastNameInstance
                    }
                    if (params.get("email.id"))
                    {
                        def emailInstance = User.get((Long)params.get("email.id"))
                        if (emailInstance)
                            emailInstance.email = emailInstance
                    }
                    if (userInstance.save(flush: true))
                        render (text: "Réussi $userInstance.id")
                    else
                        render(status: 400,text: "Echec de la mise a jour du user $userInstance.id")
                }
                else
                    render(status: 404,text: "Le user ajouté est introuvé")
                break
            case "DELETE":
                def userInstance = params.id ? User.get(params.id) : null
                if (userInstance) {
                    def userMessages = UserMessage.findAllByUser(userInstance)
                    userMessages.each {
                        UserMessage userMessage ->
                            userMessage.delete(flush: true)
                    }
                    userInstance.delete(flush: true)
                    render(status: 200, text: "user effacé")
                } else
                    render(status: 404, text: "user introuvé")
                break
            default:
                response.status = 405
                break

        }
    }
    def users()
    {
        switch (request.getMethod())
        {
            case "GET":
                reponseFormatList (User.list(), request)
                break
            case "POST":
                //verifier username
                def usernameInstance = params.author.id ? User.get(params.username.id) : null
                def userInstance
                if (usernameInstance)


                    //créer user
                    userInstance = new User(username: usernameInstance, password:params.passwordInstance, firstName: params.firstName, lastName:params.lastName, email:params.email, tel:params.tel )
                    if (userInstance.save(flush: true))




                if (response.status != 201)
                    response.status = 400

                break


        }
    }
    def messageToUser () {
        switch (request.getMethod()) {

            case "POST":
                //verifier receiver
                def receiverInstance = params.receiver.id ? User.get(params.receiver.id) : null
                def messageInstance
                if (receiverInstance)
                {

                    //créer message
                    messageInstance = new Message(receiver: receiverInstance, messageContent: params.messageContent)
                    if (messageInstance.save(flush: true))

                    {//ajouter author
                        if (params.author.id)
                        {
                            def authorInstance = User.get(params.author.id)
                            if(authorInstance)
                            new UserMessage(user: authorInstance, message: messageInstance).save(flush: true)
                        }
                        render (status: 201)
                    }
                }

                if (response.status != 201)
                    response.status = 400



                break

        }
    }

    def messageToGroup()
    {
        switch (request.getMethod())
        {

            case "POST":
                //verifier receivers-role
                def roleInstance = params.receivers.id ? Role.get(params.receivers.id) : null
                def messageInstance
                if (roleInstance)
                {

                    //créer message
                    messageInstance = new Message(receiver: receiverInstance, messageContent: params.messageContent)
                    if (messageInstance.save(flush: true))

                    {//ajouter author
                        if (params.author.id)
                        {
                            def authorInstance = User.get(params.author.id)
                            if(authorInstance)
                                new UserMessage(user: authorInstance, message: messageInstance).save(flush: true)
                        }
                        render (status: 201)
                    }
                }

                if (response.status != 201)
                    response.status = 400



                break


        }
    }

}
