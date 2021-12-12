/*
 * This file is generated by jOOQ.
 */
package de.w3is.recipes.infra.persistence.generated;


import de.w3is.recipes.infra.persistence.generated.tables.FlywaySchemaHistory;
import de.w3is.recipes.infra.persistence.generated.tables.Images;
import de.w3is.recipes.infra.persistence.generated.tables.Recipes;
import de.w3is.recipes.infra.persistence.generated.tables.Users;
import de.w3is.recipes.infra.persistence.generated.tables.records.FlywaySchemaHistoryRecord;
import de.w3is.recipes.infra.persistence.generated.tables.records.ImagesRecord;
import de.w3is.recipes.infra.persistence.generated.tables.records.RecipesRecord;
import de.w3is.recipes.infra.persistence.generated.tables.records.UsersRecord;

import org.jooq.TableField;
import org.jooq.UniqueKey;
import org.jooq.impl.DSL;
import org.jooq.impl.Internal;


/**
 * A class modelling foreign key relationships and constraints of tables in 
 * public.
 */
@SuppressWarnings({ "all", "unchecked", "rawtypes" })
public class Keys {

    // -------------------------------------------------------------------------
    // UNIQUE and PRIMARY KEY definitions
    // -------------------------------------------------------------------------

    public static final UniqueKey<FlywaySchemaHistoryRecord> FLYWAY_SCHEMA_HISTORY_PK = Internal.createUniqueKey(FlywaySchemaHistory.FLYWAY_SCHEMA_HISTORY, DSL.name("flyway_schema_history_pk"), new TableField[] { FlywaySchemaHistory.FLYWAY_SCHEMA_HISTORY.INSTALLED_RANK }, true);
    public static final UniqueKey<ImagesRecord> IMAGES_IMAGE_ID_KEY = Internal.createUniqueKey(Images.IMAGES, DSL.name("images_image_id_key"), new TableField[] { Images.IMAGES.IMAGE_ID }, true);
    public static final UniqueKey<ImagesRecord> IMAGES_PKEY = Internal.createUniqueKey(Images.IMAGES, DSL.name("images_pkey"), new TableField[] { Images.IMAGES.ID }, true);
    public static final UniqueKey<RecipesRecord> RECIPES_PKEY = Internal.createUniqueKey(Recipes.RECIPES, DSL.name("recipes_pkey"), new TableField[] { Recipes.RECIPES.ID }, true);
    public static final UniqueKey<RecipesRecord> RECIPES_RECIPE_ID_KEY = Internal.createUniqueKey(Recipes.RECIPES, DSL.name("recipes_recipe_id_key"), new TableField[] { Recipes.RECIPES.RECIPE_ID }, true);
    public static final UniqueKey<UsersRecord> USERS_PKEY = Internal.createUniqueKey(Users.USERS, DSL.name("users_pkey"), new TableField[] { Users.USERS.ID }, true);
    public static final UniqueKey<UsersRecord> USERS_USER_ID_KEY = Internal.createUniqueKey(Users.USERS, DSL.name("users_user_id_key"), new TableField[] { Users.USERS.USER_ID }, true);
    public static final UniqueKey<UsersRecord> USERS_USERNAME_KEY = Internal.createUniqueKey(Users.USERS, DSL.name("users_username_key"), new TableField[] { Users.USERS.USERNAME }, true);
}
