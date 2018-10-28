package com.base.model;

import android.os.Parcel;
import android.os.Parcelable;

public class KeyValue implements Parcelable
{

	private int id;
    private String key;

    private String value;

    public KeyValue()
    {
    }

    public KeyValue(int id, String value)
    {
    	super();
    	this.id = id;
    	this.value = value;
    }
    public KeyValue(String key, String value)
    {
        super();
        this.key = key;
        this.value = value;
    }

    public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getKey() {
		return key;
	}

	public void setKey(String key) {
		this.key = key;
	}

	public String getValue() {
		return value;
	}

	public void setValue(String value) {
		this.value = value;
	}

	@Override
    public String toString()
    {
        return value;
    }

    public static Parcelable.Creator<KeyValue> getCreator()
    {
        return CREATOR;
    }

    public int describeContents()
    {
        // TODO Auto-generated method stub
        return 0;
    }

    public void writeToParcel(Parcel dest, int flags)
    {
        // TODO Auto-generated method stub
    	dest.writeInt(id);
        dest.writeString(key);
        dest.writeString(value);
    }

    public static final Parcelable.Creator<KeyValue> CREATOR = new Creator<KeyValue>()
    {
        public KeyValue createFromParcel(Parcel source)
        {
            KeyValue instance = new KeyValue();
            instance.id = source.readInt();
            instance.key = source.readString();
            instance.value = source.readString();

            return instance;
        }

        public KeyValue[] newArray(int size)
        {
            // TODO Auto-generated method stub
            return new KeyValue[size];
        }
    };

}
