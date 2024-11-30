package com.harshi_solution.inventorymate.controllers;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.harshi_solution.inventorymate.entities.Product;
import com.harshi_solution.inventorymate.entities.Warehouse;
import com.harshi_solution.inventorymate.helper.ProductHelper;
import com.harshi_solution.inventorymate.service.ProductService;
import com.harshi_solution.inventorymate.service.WarehouseService;
import com.harshi_solution.inventorymate.util.Constants;

/**
 * Controller class for managing product-related operations.
 */
@Controller
public class ProductController {

	private static final Logger LOGGER = LoggerFactory.getLogger(ProductController.class);

	
	@Autowired
	private ProductService productService;

	@Autowired
	private WarehouseService warehouseService;

	/**
	 * This is an empty product attribute so that the form can use this while
	 * rendering.
	 *
	 * @return A new Product instance.
	 */
	@ModelAttribute(Constants.PRODUCT)
	public Product getProduct() {
		return new Product();
	}

	/**
	 * Display the list of available products.
	 *
	 * @param modelMap ModelMap for adding attributes to the view.
	 * @return The view name for the product list.
	 */
	@RequestMapping("/showProductsList")
	public String showProductsList(ModelMap modelMap) {
		LOGGER.info("Displaying the list of available products");
		// Retrieve available products and add them to the modelMap
		List<Product> productsList = productService.getProductsList();
		modelMap.addAttribute(Constants.PRODUCTS_LIST, productsList);
		return Constants.PRODUCT_VIEW_FOLDER+Constants.PRODUCTS_LIST;
	}

	/**
	 * Display the form for adding new products.
	 *
	 * @param model Model for adding attributes to the view.
	 * @return The view name for the add products form.
	 */
	@RequestMapping("/showAddProducts")
	public String showAddProducts(Model model) {
		LOGGER.info("Displaying the add products form");
		List<Warehouse> warehouses = warehouseService.getWarehousesList();
		model.addAttribute(Constants.WAREHOUSES, warehouses);
		return Constants.PRODUCT_VIEW_FOLDER+Constants.ADD_PRODUCTS;
	}

	/**
	 * Handle the addition of new products.
	 *
	 * @param product  The Product object to be added.
	 * @param modelMap ModelMap for adding attributes to the view.
	 * @return The view name for the product list or the error view in case of an
	 *         exception.
	 */
	@RequestMapping("/addProducts")
	public String addProducts(@ModelAttribute(Constants.PRODUCT) Product product, ModelMap modelMap) {
		LOGGER.info("Saving a new product");
		try {
			// Convert brand name and product type to uppercase
			product.setBrandName(product.getBrandName().toUpperCase());
			product.setPType(product.getPType().toUpperCase());

			// Save the product with warehouse quantities
			Product savedProduct = productService.saveProduct(product);
			List<Product> productsList = productService.getProductsList();
			modelMap.addAttribute(Constants.PRODUCTS_LIST, productsList);
			String msg = "Product saved with ID - " + savedProduct.getProductId();
			modelMap.addAttribute(Constants.MSG, msg);
			return Constants.PRODUCT_VIEW_FOLDER+Constants.PRODUCTS_LIST;
		} catch (Exception e) {
			// Handle the exception
			e.printStackTrace(); // You can log the exception for debugging purposes

			// Add an error message to the model to display to the user
			modelMap.addAttribute(Constants.ERROR, "An error occurred while saving the product. Please try again.");
			modelMap.addAttribute(Constants.ERROR_MESSAGE, e.getMessage());
			return Constants.ERROR; // Return to the error view with the error message
		}
	}

	/**
	 * Display the details of a specific product by productId.
	 *
	 * @param productId The ID of the product to display.
	 * @param model     Model for adding attributes to the view.
	 * @return The view name for the product details.
	 */
	@GetMapping("/productDetails/{productId}")
	public String showProductDetails(@PathVariable Long productId, Model model) {
		LOGGER.info("Displaying product details for product with ID: {}", productId);
		Product product = productService.getProductById(productId);
		List<Warehouse> warehouses = warehouseService.getWarehousesList();
		Integer totalProductQuantity = ProductHelper.totalProductQuantity(product);
		model.addAttribute(Constants.TOTAL_PRODUCT_QUANTITY, totalProductQuantity);
		model.addAttribute(Constants.PRODUCT, product);
		model.addAttribute(Constants.WAREHOUSES, warehouses);
		return Constants.PRODUCT_VIEW_FOLDER+Constants.PRODUCT_DETAILS;
	}

	/**
	 * Edit product quantities for a specific product by productId.
	 *
	 * @param productId The ID of the product to edit quantities for.
	 * @param model     Model for adding attributes to the view.
	 * @return The view name for editing product quantities.
	 */
	@GetMapping("/editProductQuantities/{productId}")
	public String editProductQuantities(@PathVariable Long productId, Model model) {
		LOGGER.info("Editing product quantities for product with ID: {}", productId);
		Product product = productService.getProductById(productId);
		List<Warehouse> warehouses = warehouseService.getWarehousesList();
		Integer totalProductQuantity = ProductHelper.totalProductQuantity(product);
		model.addAttribute(Constants.TOTAL_PRODUCT_QUANTITY, totalProductQuantity);
		model.addAttribute(Constants.PRODUCT, product);
		model.addAttribute(Constants.WAREHOUSES, warehouses);
		return Constants.PRODUCT_VIEW_FOLDER+Constants.EDIT_PRODUCT_QUANTITIES;
	}

	/**
	 * Update product quantities in the database for a specific product by
	 * productId.
	 *
	 * @param product  The Product object containing updated quantities.
	 * @param modelMap ModelMap for adding attributes to the view.
	 * @return The view name for the product list.
	 */
	@PostMapping("/updateProductQuantities")
	public String updateProductQuantities(@ModelAttribute(Constants.PRODUCT) Product product, ModelMap modelMap) {
		LOGGER.info("Updating product quantities for product with ID: {}", product.getProductId());
		// Update the product quantities in the database
		Product fetchedProduct = productService.getProductById(product.getProductId());
		fetchedProduct.setWarehouseQuantities(product.getWarehouseQuantities());
		productService.saveProduct(fetchedProduct);
		List<Product> productsList = productService.getProductsList();
		modelMap.addAttribute(Constants.PRODUCTS_LIST, productsList);
		String msg = "Product updated with ID - " + product.getProductId();
		modelMap.addAttribute(Constants.MSG, msg);
		return Constants.PRODUCT_VIEW_FOLDER+Constants.PRODUCTS_LIST;
	}
}
