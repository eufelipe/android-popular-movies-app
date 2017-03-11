package com.eufelipe.popularmovies.bases;


import android.util.Log;

import com.eufelipe.popularmovies.application.Constants;

import org.json.JSONObject;

import java.lang.reflect.Field;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Map;

public class BaseModel {

    public boolean jsonForObject(Object object, JSONObject jsonObject) {
        try {
            for (Field field : object.getClass().getDeclaredFields()) {
                String fieldName = field.getName();
                Object value = jsonObject.get(fieldName);

                if (value == null) {
                    continue;
                }

                field.setAccessible(true);
                parse(field, object, value);

            }
        } catch (Exception e) {
            Log.d(Constants.LOG_TAG_ERROR, e.getMessage());
            return false;
        }
        return true;
    }

    public boolean mapForObject(Object object, Map<String, Object> mapValue) {
        try {
            for (Field field : object.getClass().getDeclaredFields()) {
                String fieldName = field.getName();
                Object value = mapValue.get(fieldName);

                if (value == null) {
                    continue;
                }

                field.setAccessible(true);
                parse(field, object, value);
            }
        } catch (Exception e) {
            Log.d(Constants.LOG_TAG_ERROR, e.getMessage());
            return false;
        }
        return true;
    }


    public boolean parse(Field field, Object object, Object value) {
        try {
            if (field.getType().equals(Date.class)) {
                field.set(object, convertW3CToDate(value.toString()));

            } else if (field.getType().equals(Boolean.class)) {
                field.set(object, value.toString().equals("true"));

            } else if (field.getType().equals(String.class)) {
                field.set(object, value);

            } else if (field.getType().equals(Integer.class)) {
                field.set(object, value);

            } else if (field.getType().equals(Double.class)) {
                field.set(object, value);

            } else if (field.getType().equals(Float.class)) {
                field.set(object, value);

            } else if (field.getType().equals(Float.class)) {
                field.set(object, value);

            } else if (field.getType().equals(Long.class)) {
                field.set(object, value);

            } else if (field.getType().equals(Map.class)) {
                field.set(object, value);

            } else if (field.getType().equals(List.class)) {
                field.set(object, value);
            }
        } catch (IllegalAccessException e) {
            return false;
        }
        return true;
    }


    public Date convertW3CToDate(Object objDate) {
        if (!(objDate instanceof String)) {
            return null;
        }

        String strDate = objDate.toString();

        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");
        try {
            return simpleDateFormat.parse(strDate);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return null;
    }


}
