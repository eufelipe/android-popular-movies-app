package com.eufelipe.popularmovies.migrations;


import io.realm.DynamicRealm;
import io.realm.RealmMigration;
import io.realm.RealmSchema;

/**
 * Class helper para gerenciar versões do db
 */
public class ManageMigration {

    public static final long MIGRATION_VERSION = 1;

    public RealmMigration migrate() {
        return new RealmMigration() {
            @Override
            public void migrate(DynamicRealm realm, long oldVersion, long newVersion) {

                RealmSchema schema = realm.getSchema();

                // Version 1
                if (oldVersion == 0) {
                    oldVersion++;
                }

            }
        };
    }

}
