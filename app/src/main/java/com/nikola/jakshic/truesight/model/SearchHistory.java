package com.nikola.jakshic.truesight.model;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.Index;
import android.arch.persistence.room.PrimaryKey;

@Entity(indices = {@Index(value = "query", unique = true)})
public class SearchHistory {

    @PrimaryKey(autoGenerate = true)
    private int id;
    @ColumnInfo(collate = ColumnInfo.NOCASE)
    private String query;

    public SearchHistory(String query) {
        this.query = query;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getId() {
        return id;
    }

    public String getQuery() {
        return query;
    }
}