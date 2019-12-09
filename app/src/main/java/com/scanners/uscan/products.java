//Add to package
package com.scanners.uscan;
// This class simply creates an object that will obtain data from the database and store it for use by any of 3 methods.
public class products {
    //Declare variables
    public String name, price, region, link, image, description;

    //Getters and Setters
    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }

    public String getPrice() {
        return price;
    }
    public void setPrice(String price) {
        this.price = price;
    }

    public String getRegion() {
        return region;
    }
    public void setRegion(String region) {
        this.region = region;
    }

    public String getLink() { return link; }
    public void setLink(String link) { this.link = link; }

    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }

    public String getImage() {
        return image;
    }
    public void setImage(String image) { this.image = image; }

    //Constructor
    public products(String Name, String Price, String Region, String Link, String Image, String Description ) {
        this.name = Name;
        this.price = Price;
        this.region = Region;
        this.link = Link;
        this.image = Image;
        this.description = Description;
    }
    //Empty constructor
    public products(){}

}//End class
