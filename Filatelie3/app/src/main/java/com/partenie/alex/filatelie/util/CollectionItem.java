package com.partenie.alex.filatelie.util;

import android.os.Parcel;
import android.os.Parcelable;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class CollectionItem implements Parcelable {
    public Integer id;
    public String imgLocation;
    public String name;
    public String description;
    public Float price;
    public Date manufacturedDate;
    public Date acquiredDate;
    public String historicLocation;
    public String type;

    public CollectionItem() {
    }

    public CollectionItem(Integer id, String imgLocation, String name, String description, Float price, Date manufacturedDate, String historicLocation, String type) {
        this.id = id;
        this.imgLocation = imgLocation;
        this.name = name;
        this.description = description;
        this.price = price;
        this.manufacturedDate = manufacturedDate;
        this.acquiredDate = Calendar.getInstance().getTime();
        this.historicLocation = historicLocation;
        this.type = type;
    }

    public static final Creator<CollectionItem> CREATOR = new Creator<CollectionItem>() {
        @Override
        public CollectionItem createFromParcel(Parcel in) {
            return new CollectionItem(in);
        }

        @Override
        public CollectionItem[] newArray(int size) {
            return new CollectionItem[size];
        }
    };

    public int getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getImgLocation() {
        return imgLocation;
    }

    public void setImgLocation(String imgLocation) {
        this.imgLocation = imgLocation;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public float getPrice() {
        return price;
    }

    public void setPrice(Float price) {
        this.price = price;
    }

    public Date getManufacturedDate() {
        return manufacturedDate;
    }

    public void setManufacturedDate(Date manufacturedDate) {
        this.manufacturedDate = manufacturedDate;
    }

    public Date getAcquiredDate() {
        return acquiredDate;
    }

    public void setAcquiredDate(Date acquiredDate) {
        this.acquiredDate = acquiredDate;
    }

    public String getHistoricLocation() {
        return historicLocation;
    }

    public void setHistoricLocation(String historicLocation) {
        this.historicLocation = historicLocation;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    @Override
    public String toString() {
        return "Obiectul " + name + '\'' +
                ", cu pretul" + price +
                ", din '" + historicLocation + '\'' +
                ", de tip " + type;
    }



    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeInt(id);
        parcel.writeString(imgLocation);
        parcel.writeString(name);
        parcel.writeString(description);
        parcel.writeFloat(price);
        parcel.writeString(new SimpleDateFormat("dd-MM-yyyy", Locale.US).format(manufacturedDate));
        parcel.writeString(new SimpleDateFormat("dd-MM-yyyy", Locale.US).format(acquiredDate));
        parcel.writeString(historicLocation);
        parcel.writeString(type);
    }

    private CollectionItem(Parcel in) {

        if (in.readByte() == 0) {
            id = null;
        } else {
            id = in.readInt();
        }
        imgLocation = in.readString();
        name = in.readString();
        description = in.readString();
        if (in.readByte() == 0) {
            price = null;
        } else {
            price = in.readFloat();
        }
        try {
            this.manufacturedDate = new SimpleDateFormat(
                    "dd-MM-yyyy",
                    Locale.US).parse(in.readString());
        } catch (ParseException e) {
            e.printStackTrace();
        }
        try {
            this.acquiredDate = new SimpleDateFormat(
                    "dd-MM-yyyy",
                    Locale.US).parse(in.readString());
        } catch (ParseException e) {
            e.printStackTrace();
        }
        historicLocation = in.readString();
        type = in.readString();
    }


}
