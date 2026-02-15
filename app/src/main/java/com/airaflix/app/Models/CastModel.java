package com.airaflix.app.Models;

import android.os.Parcel;
import android.os.Parcelable;

import androidx.annotation.NonNull;

public class CastModel implements Parcelable {

    public boolean adult;
    public int gender;
    public int id;
    public String known_for_department;
    public String name;
    public String original_name;
    public double popularity;
    public String profile_path;
    public int cast_id;
    public String character;
    public String credit_id;
    public int order;

    public CastModel(boolean adult, int gender, int id, String known_for_department, String name, String original_name, double popularity, String profile_path, int cast_id, String character, String credit_id, int order) {
        this.adult = adult;
        this.gender = gender;
        this.id = id;
        this.known_for_department = known_for_department;
        this.name = name;
        this.original_name = original_name;
        this.popularity = popularity;
        this.profile_path = profile_path;
        this.cast_id = cast_id;
        this.character = character;
        this.credit_id = credit_id;
        this.order = order;
    }

    protected CastModel(Parcel in) {
        adult = in.readByte() != 0;
        gender = in.readInt();
        id = in.readInt();
        known_for_department = in.readString();
        name = in.readString();
        original_name = in.readString();
        popularity = in.readDouble();
        profile_path = in.readString();
        cast_id = in.readInt();
        character = in.readString();
        credit_id = in.readString();
        order = in.readInt();
    }

    public static final Creator<CastModel> CREATOR = new Creator<CastModel>() {
        @Override
        public CastModel createFromParcel(Parcel in) {
            return new CastModel(in);
        }

        @Override
        public CastModel[] newArray(int size) {
            return new CastModel[size];
        }
    };

    public boolean isAdult() {
        return adult;
    }

    public int getGender() {
        return gender;
    }

    public int getId() {
        return id;
    }

    public String getKnown_for_department() {
        return known_for_department;
    }

    public String getName() {
        return name;
    }

    public String getOriginal_name() {
        return original_name;
    }

    public double getPopularity() {
        return popularity;
    }

    public String getProfile_path() {
        return profile_path;
    }

    public int getCast_id() {
        return cast_id;
    }

    public String getCharacter() {
        return character;
    }

    public String getCredit_id() {
        return credit_id;
    }

    public int getOrder() {
        return order;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(@NonNull Parcel dest, int flags) {
        dest.writeByte((byte) (adult ? 1 : 0));
        dest.writeInt(gender);
        dest.writeInt(id);
        dest.writeString(known_for_department);
        dest.writeString(name);
        dest.writeString(original_name);
        dest.writeDouble(popularity);
        dest.writeString(profile_path);
        dest.writeInt(cast_id);
        dest.writeString(character);
        dest.writeString(credit_id);
        dest.writeInt(order);
    }
}
