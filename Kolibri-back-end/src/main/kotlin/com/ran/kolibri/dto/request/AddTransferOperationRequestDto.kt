package com.ran.kolibri.dto.request

class AddTransferOperationRequestDto : AddOperationRequestDto() {

    var fromAccountId: Long? = null
    var toAccountId: Long? = null

}
