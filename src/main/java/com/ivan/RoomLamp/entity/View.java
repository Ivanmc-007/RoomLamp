package com.ivan.RoomLamp.entity;

public final class View {

    public interface Id {}

    public interface Lamp {}

    public interface IdName extends Id {}

    public interface IdNameCountry extends IdName {}

    public interface IdNameCountryLamp extends IdNameCountry {}

}
