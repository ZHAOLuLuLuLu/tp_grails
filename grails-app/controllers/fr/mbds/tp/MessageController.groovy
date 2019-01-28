package fr.mbds.tp

import grails.validation.ValidationException
import static org.springframework.http.HttpStatus.*


class MessageController {

    MessageService messageService

    static allowedMethods = [save: "POST", update: "PUT", delete: "DELETE"]

    def index(Integer max) {
        params.max = Math.min(max ?: 10, 100)
        respond messageService.list(params), model:[messageCount: messageService.count()]
    }

    def show(Long id) {
        def messageInstance = UserMessage.get(id)
        def userMessageList = UserMessage.findAllByMessage(messageInstance)
        def userList = userMessageList.collect{it.user}
        respond messageInstance,model: [userList: userList]
    }

    def create() {
        respond new Message(params)
    }

    def save(Message message) {

        if (message == null) {
            notFound()
            return
        }

        try {
            messageService.save(message)
        } catch (ValidationException e) {
            respond message.errors, view:'create'
            return
        }
        //récupéter l'id du desdetinataire
        //instancier ce dernier
        //créer une instance de UserMessage correspondant à l'envol de ce message
        //persister l'instance UserMessage nouvellement créée

        //si groupe spècifié:
        //récupérer l'instance de Role désigné
        //créer un nouveau userMessage pour tous les utillisateurs dudit groupe

        request.withFormat {
            form multipartForm {
                flash.message = "Le message est correctement mise à jour (id: ${message.id})"
                        //message(code: 'default.created.message', args: [message(code: 'message.label', default: 'Message'), message.id])
                redirect message
            }
            '*' { respond message, [status: CREATED] }
        }
    }

    def edit(Long id) {
        respond messageService.get(id)
    }

    def update(Message message) {
        if (message == null) {
            notFound()
            return
        }

        try {
            messageService.save(message)
        } catch (ValidationException e) {
            respond message.errors, view:'edit'
            return
        }

        request.withFormat {
            form multipartForm {
                flash.message = "Le message est correctement mise à jour (id: ${message.id})"
                redirect message
            }
            '*'{ respond message, [status: OK] }
        }
    }

    def delete(Long id) {
        if (id == null) {
            notFound()
            return
        }

        messageService.delete(id)

        request.withFormat {
            form multipartForm {
                flash.message = message(code: 'default.deleted.message', args: [message(code: 'message.label', default: 'Message'), id])
                redirect action:"index", method:"GET"
            }
            '*'{ render status: NO_CONTENT }
        }
    }

    protected void notFound() {
        request.withFormat {
            form multipartForm {
                flash.message = message(code: 'default.not.found.message', args: [message(code: 'message.label', default: 'Message'), params.id]) as Object
                redirect action: "index", method: "GET"
            }
            '*'{ render status: NOT_FOUND }
        }
    }
}
