package de.w3is.recipes.utils

import de.w3is.recipes.infra.persistence.generated.DefaultSchema
import org.jooq.DSLContext

interface DBCleaner {

    val dslContext: DSLContext

    fun cleanDb() {
        DefaultSchema.DEFAULT_SCHEMA.tables.forEach { table ->
            dslContext.deleteFrom(table).execute()
        }
    }
}
