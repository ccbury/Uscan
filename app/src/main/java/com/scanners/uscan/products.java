//Add to package
package com.scanners.uscan;
// This class simply creates an object that will obtain data from the database and store it for use by any of 3 methods.
public class products {
    //Declare variables
    public String Name, Price, Region, Link, Image, Description;

    //Getters and Setters
    public String getName() {
        return Name;
    }
    public void setName(String Name) {
        this.Name = Name;
    }

    public String getPrice() {
        return Price;
    }
    public void setPrice(String Price) {
        this.Price = Price;
    }

    public String getRegion() {
        return Region;
    }
    public void setRegion(String Region) {
        this.Region = Region;
    }

    public String getLink() { return Link; }
    public void setLink(String Link) { this.Link = Link; }

    public String getDescription() { return Description; }
    public void setDescription(String Description) { this.Description = Description; }

    public String getImage() {
        return Image;
    }
    public void setImage(String Image) { this.Image = Image; }

    //Constructor
    public products(String Name, String Price, String Region, String Link, String Image, String Description ) {
        this.Name = Name;
        this.Price = Price;
        this.Region = Region;
        this.Link = Link;
        this.Image = Image;
        this.Description = Description;
    }
    //Empty constructor
    public products(){}

}//End class
