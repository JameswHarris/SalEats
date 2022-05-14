package Util;

import java.util.ArrayList;
/**
 * The class used to model a business
 */
public class Business {
	private String id;
	private String name;
	private String image_url;
	private double rating;
	private Location location;
	private ArrayList<Category> categories = new ArrayList<Category>();
	private String price;
	private String phone;
	private String url;
	private int review_count;
	
	public Business(String id, String name) {
		this.id = id;
		this.name = name;
	}
	
    public Business(String id, String name, String image_url, double rating, String location, ArrayList<String> categories, String price, String phone, String url, int reviewCount) {
    	 this.id = id;
    	 this.name = name;
    	 this.image_url = image_url;
    	 this.rating = rating;
    	 this.location = new Location(location);
    	 for(String cat: categories) {
    		 this.categories.add(new Category(cat));
    	 }
    	 this.price = price;
    	 this.phone = phone;
    	 this.url = url;
    	 this.review_count = reviewCount;
    }
    
    public String getID() {
    	return id;
    }
    
    public String getName() {
    	return name;
    }
    
    public String getimage_url() {
    	return image_url;
    }
    
    public double getRating() {
    	return rating;
    }
    
    public String getLocation() {
    	return location.getDisplay_address();
    }
    
    public String turnCategoriesIntoList() {
    	String result = "";
    	for(Category cat:categories) {
    		result+=cat.getTitle();
    	}
    	return result;
    	
    }
    public ArrayList<String> getCategories() {
    	ArrayList<String> result = new ArrayList<String>();
    	for(Category cat:categories) {
    		result.add(cat.getTitle());
    	}
    	return result;
    }
    
    public String getPrice() {
    	return price;
    }
    
    public String getPhone() {
    	return phone;
    }
    
    public String getUrl() {
    	return url;
    }
    
    public int getReviewCount() {
    	return review_count;
    }
    
    public void addCategory(String newCat) {
    	Category cat = new Category(newCat);
    	categories.add(cat);
    }
    
}

class Category {
	private String title;
	
	public Category(String title) {
		this.title = title;
	}
	
	public String getTitle() {
		return title;
	}
}

class Location{
	private ArrayList<String> display_address = new ArrayList<String>();
	
	public Location(String location) {
		display_address.add(location);
	}
	public String getDisplay_address() {
		String result = "";
		for(String display: display_address) {
			result += display;
		}
		return result;
	}

}
