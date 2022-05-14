DROP DATABASE IF EXISTS PA2;
CREATE DATABASE PA2;
USE PA2;

CREATE TABLE Rating_details(
  rating_id INT PRIMARY KEY NOT NULL AUTO_INCREMENT,
  review_count INT NOT NULL,
  rating INT
);
CREATE TABLE Restaurant_details(
  details_id INT PRIMARY KEY NOT NULL AUTO_INCREMENT,
  images_url VARCHAR(500) NOT NULL,
  address VARCHAR(500),
  phone_no VARCHAR(20),
  estimated_price VARCHAR(10),
  yelp_url VARCHAR(1500)
);
CREATE TABLE Restaurant(
  restaurant_id VARCHAR(500) PRIMARY KEY NOT NULL,
  restaurant_name VARCHAR(500),
  details_id INT,
  rating_id INT
);

CREATE TABLE Category(
  category_id INT PRIMARY KEY NOT NULL auto_increment,
  category_name VARCHAR(500) NOT NULL,
  restaurant_id VARCHAR(500)
);

CREATE TABLE Users(
  user_id INT PRIMARY KEY NOT NULL AUTO_INCREMENT,
  email VARCHAR(100) NOT NULL,
  name VARCHAR(100) NOT NULL,
  password VARCHAR(100) NOT NULL
);

ALTER TABLE Restaurant
ADD FOREIGN KEY (details_id) REFERENCES Restaurant_details(details_id); 
ALTER TABLE Restaurant
ADD FOREIGN KEY (rating_id) REFERENCES Rating_details(rating_id);
ALTER TABLE Category
ADD FOREIGN KEY (restaurant_id) REFERENCES Restaurant(restaurant_id);