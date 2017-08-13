package com.ran.kolibri.dto.request

class AddTransferOperationRequestDTO : AddOperationRequestDTO() {

    var fromAccountId: Long? = null
    var toAccountId: Long? = null

}