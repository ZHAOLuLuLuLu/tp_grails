package fr.mbds.tp

class Message {

    String messageContent

    Date dateCreated

    User author

    static constraints = {
        messageContent nullable: false, blank: false
        author nullable: false
    }





}
