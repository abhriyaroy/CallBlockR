package com.abhriya.callblockr.domain.mapper

import com.abhriya.callblockr.data.entity.CallLogEntity
import com.abhriya.callblockr.domain.model.CallLogModel

internal object CallLogMapper {
    internal fun mapCallLogEntityToCallLogModel(
        callLogEntity: CallLogEntity,
        isNumberBlocked: Boolean
    ): CallLogModel {
        return CallLogModel(
            callLogEntity.contactName,
            callLogEntity.contactNumber,
            callLogEntity.callType,
            callLogEntity.timeStampInMillis,
            callLogEntity.callDuration,
            isNumberBlocked
        )
    }

    internal fun mapCallLogModelToCallLogEntity(callLogModel: CallLogModel): CallLogEntity {
        return CallLogEntity(
            callLogModel.contactName,
            callLogModel.contactNumber,
            callLogModel.callType,
            callLogModel.timeStampInMillis,
            callLogModel.callDuration,
        )
    }
}