package com.ran.kolibri.dto.request.financial

class AddTransferOperationRequestDto : AddOperationRequestDto() {

    var fromAccountId: Long? = null
    var toAccountId: Long? = null

}
