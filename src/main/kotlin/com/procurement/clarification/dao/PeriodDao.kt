package com.procurement.clarification.dao

import com.datastax.driver.core.Session
import com.datastax.driver.core.querybuilder.QueryBuilder.*
import com.procurement.clarification.exception.ErrorException
import com.procurement.clarification.exception.ErrorType
import com.procurement.clarification.model.entity.PeriodEntity
import org.springframework.stereotype.Service

interface PeriodDao {

    fun save(entity: PeriodEntity)

    fun getByCpIdAndStage(cpId: String, stage: String): PeriodEntity

}

@Service
class PeriodDaoImpl(private val session: Session) : PeriodDao {

    override fun save(entity: PeriodEntity) {
        val insert =
                insertInto(PERIOD_TABLE)
                        .value(CP_ID, entity.cpId)
                        .value(STAGE, entity.stage)
                        .value(START_DATE, entity.startDate)
                        .value(END_DATE, entity.endDate)
                        .value(TENDER_END_DATE, entity.tenderEndDate)
        session.execute(insert)
    }

    override fun getByCpIdAndStage(cpId: String, stage: String): PeriodEntity {
        val query = select()
                .all()
                .from(PERIOD_TABLE)
                .where(eq(CP_ID, cpId))
                .and(eq(STAGE, stage)).limit(1)
        val row = session.execute(query).one()
        return if (row != null)
            PeriodEntity(
                    cpId = row.getString(CP_ID),
                    stage = row.getString(STAGE),
                    startDate = row.getTimestamp(START_DATE),
                    endDate = row.getTimestamp(END_DATE),
                    tenderEndDate = row.getTimestamp(TENDER_END_DATE)
            )
        else throw ErrorException(ErrorType.PERIOD_NOT_FOUND)
    }

    companion object {
        private val PERIOD_TABLE = "clarification_period"
        private val CP_ID = "cp_id"
        private val STAGE = "stage"
        private val START_DATE = "start_date"
        private val END_DATE = "end_date"
        private val TENDER_END_DATE = "tender_end_date"
    }
}