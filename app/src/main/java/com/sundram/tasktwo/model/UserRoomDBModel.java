package com.sundram.tasktwo.model;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

import com.sundram.tasktwo.utils.ConstantUtils;

@Entity(tableName = ConstantUtils.TABLE_NAME)
public class UserRoomDBModel {

    @PrimaryKey
    private Integer id;

    private Response response;

    public UserRoomDBModel(Integer id, Response response) {
        this.id = id;
        this.response = response;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Response getResponse() {
        return response;
    }

    public void setResponse(Response response) {
        this.response = response;
    }
}
