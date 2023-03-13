package services;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import models.Category;

import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;

public class CategoriesApi {

	public List<Category> getChildrenCategoriesFor(String categId) {
		RestTemplate restTemplate = new RestTemplate();
		String url = "https://api.mercadolibre.com/categories/" + categId;
		String categoriesResponse = restTemplate.getForObject(url, String.class);

		JsonParser parser = new JsonParser();
		JsonElement domainsElement = parser.parse(categoriesResponse);
		JsonElement categsElement = domainsElement.getAsJsonObject().get("children_categories");

		Gson gson = new Gson();
		Category[] categs = gson.fromJson(categsElement, Category[].class);

		return Arrays.asList(categs);
	}

	public List<Category> getCategories() {
		RestTemplate restTemplate = new RestTemplate();
		String url = "https://api.mercadolibre.com/sites/MLA/categories";
		ResponseEntity<Category[]> categoriesResponse = restTemplate.getForEntity(url, Category[].class);

		return Arrays.asList(categoriesResponse.getBody());
	}

	public List<Category> getAllCategories() {
		List<Category> all = new ArrayList<Category>();
		List<Category> categsLevel1 = getCategories();
		all.addAll(categsLevel1);

		for (Category category : categsLevel1) {
			List<Category> categories = getAllCategoriesAux(category,2);
			all.addAll(categories);
		}

		return all;
	}

	public List<Category> getAllCategoriesAux(Category node, int level) {
		List<Category> all = new ArrayList<Category>();

		List<Category> categoriesForThisLevel = getChildrenCategoriesFor(node.getId());
		//all.addAll(categoriesForThisLevel);

		level++;
		System.out.println("Level:" + level);
		System.out.println("Parent:" + node);
		all.add(node);
		for (Category category : categoriesForThisLevel) {
			
			System.out.println("Children:"+category);
			List<Category> categories = getAllCategoriesAux(category, level);
			all.addAll(categories);
		}

		return all;
	}

	public static void main(String[] args) {
		CategoriesApi categoriesApi = new CategoriesApi();
		// MLA1168
		//for (Category categ : categoriesApi.getAllCategoriesAux(new Category("MLA1631", "Decoracion hogar"))) {
		for (Category categ : categoriesApi.getAllCategories()) {

			System.out.println(categ);
		}
		System.out.println("fin");
	}
}
