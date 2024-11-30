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
import com.harshi_solution.inventorymate.helper.WarehouseHelper;
import com.harshi_solution.inventorymate.service.ProductService;
import com.harshi_solution.inventorymate.service.WarehouseService;
import com.harshi_solution.inventorymate.util.Constants;

/**
 * Controller class for managing warehouse-related operations.
 */
@Controller
public class WarehouseController {

	@Autowired
	private ProductService productService;

	private static final Logger LOGGER = LoggerFactory.getLogger(WarehouseController.class);

	@Autowired
	private WarehouseService warehouseService;

	/**
	 * An empty warehouse attribute so that the form can use this while rendering.
	 *
	 * @return An empty Warehouse object.
	 */
	@ModelAttribute(Constants.WAREHOUSE)
	public Warehouse getWarehouse() {
		return new Warehouse();
	}

	/**
	 * Display the list of warehouses.
	 *
	 * @param modelMap ModelMap for adding attributes to the view.
	 * @return The view name for the list of warehouses.
	 */
	@RequestMapping("/showWarehouseList")
	public String showWarehouseList(ModelMap modelMap) {
		LOGGER.info("Inside showWarehouseList() on WarehouseController");
		// Retrieve all warehouses and add them to the modelMap
		List<Warehouse> warehousesList = warehouseService.getWarehousesList();
		modelMap.addAttribute(Constants.WAREHOUSES_LIST, warehousesList);
		return Constants.WAREHOUSE_VIEW + Constants.WAREHOUSES_LIST;
	}

	/**
	 * Display the add warehouse form.
	 *
	 * @return The view name for the add warehouse form.
	 */
	@RequestMapping("/showAddWarehouse")
	public String showAddWarehouse() {
		LOGGER.info("Inside showAddWarehouse() on WarehouseController");
		return Constants.WAREHOUSE_VIEW + Constants.ADD_WAREHOUSE;
	}

	/**
	 * Add a new warehouse.
	 *
	 * @param warehouse The Warehouse object to be added.
	 * @param modelMap  ModelMap for adding attributes to the view.
	 * @return The view name for the list of warehouses or an error view in case of
	 *         an exception.
	 */
	@RequestMapping("/addWarehouse")
	public String addWarehouse(@ModelAttribute(Constants.WAREHOUSE) Warehouse warehouse, ModelMap modelMap) {
		try {
			// Convert warehouse data to uppercase
			warehouse.setWareCode(warehouse.getWareCode().toUpperCase());
			warehouse.setWareName(warehouse.getWareName().toUpperCase());

			// Attempt to save the warehouse
			Warehouse savedWarehouse = warehouseService.saveWarehouse(warehouse);

			// Fetch the list of all warehouses
			List<Warehouse> warehousesList = warehouseService.getWarehousesList();

			// Add the warehouses list to the model
			modelMap.addAttribute(Constants.WAREHOUSES_LIST, warehousesList);

			// Create a success message
			String msg = "Warehouse saved with ID - " + savedWarehouse.getWareId();

			// Add the success message to the model
			modelMap.addAttribute(Constants.MSG, msg);

			// Log a message indicating a successful warehouse addition
			LOGGER.info(msg);

			return Constants.WAREHOUSE_VIEW + Constants.WAREHOUSES_LIST;
		} catch (Exception e) {
			// Handle the exception
			e.printStackTrace(); // You can log the exception for debugging purposes

			// Add an error message to the model to display to the user
			modelMap.addAttribute(Constants.ERROR, "An error occurred while adding the warehouse. Please try again.");
			modelMap.addAttribute(Constants.ERROR_MESSAGE, e.getMessage());
			return Constants.ERROR; // Return to the error view with the error message
		}
	}

	/**
	 * Displays details of a warehouse identified by its unique ID.
	 *
	 * @param warehouseId The unique identifier of the warehouse.
	 * @param model       Model for adding attributes to the view.
	 * @return The view name for the warehouse details or an error page in case of
	 *         an exception.
	 */
	@GetMapping("/warehouseDetails/{warehouseId}")
	public String showWarehouseDetails(@PathVariable Long warehouseId, Model model) {
		try {
			// Retrieve the warehouse by ID
			Warehouse warehouse = warehouseService.getWarehouseById(warehouseId);

			// Fetch the list of products
			List<Product> productsList = productService.getProductsList();

			Integer totalProductQuantity = WarehouseHelper.totalProductQuantity(warehouse);

			// Add the warehouse and products list to the model
			model.addAttribute(Constants.WAREHOUSE, warehouse);
			model.addAttribute(Constants.PRODUCTS_LIST, productsList);
			model.addAttribute(Constants.TOTAL_PRODUCT_QUANTITY, totalProductQuantity);

			// Log a message indicating a successful retrieval of warehouse details
			LOGGER.info("Viewing warehouse details for Warehouse ID: {}", warehouseId);

			return Constants.WAREHOUSE_VIEW + "warehouseDetails";
		} catch (Exception e) {
			// Log an error message if an exception occurs during the process
			LOGGER.error("Error viewing warehouse details: {}", e.getMessage());

			// You might also want to handle the exception, display an error message, or
			// redirect accordingly
			return "errorPage"; // Adjust this to your error handling strategy
		}
	}

	/**
	 * Displays a form to edit the quantities of products in a specific warehouse.
	 *
	 * @param warehouseId The unique identifier of the warehouse.
	 * @param model       Model for adding attributes to the view.
	 * @return The view name for editing warehouse quantities or an error page in
	 *         case of an exception.
	 */
	@GetMapping("/editWarehouseQuantities/{warehouseId}")
	public String editWarehouseQuantities(@PathVariable Long warehouseId, Model model) {
		try {
			// Retrieve the warehouse by ID
			Warehouse warehouse = warehouseService.getWarehouseById(warehouseId);

			// Fetch the list of products
			List<Product> productsList = productService.getProductsList();

			Integer totalProductQuantity = WarehouseHelper.totalProductQuantity(warehouse);

			// Add the warehouse and products list to the model
			model.addAttribute(Constants.WAREHOUSE, warehouse);
			model.addAttribute(Constants.PRODUCTS_LIST, productsList);
			model.addAttribute(Constants.TOTAL_PRODUCT_QUANTITY, totalProductQuantity);

			// Log a message indicating a successful retrieval of warehouse and products
			LOGGER.info("Editing warehouse quantities for Warehouse ID: {}", warehouseId);

			return "warehouseView/editWarehouseQuantities";
		} catch (Exception e) {
			// Log an error message if an exception occurs during the process
			LOGGER.error("Error editing warehouse quantities: {}", e.getMessage());

			// You might also want to handle the exception, display an error message, or
			// redirect accordingly
			return "errorPage"; // Adjust this to your error handling strategy
		}
	}

	/**
	 * Handles the update of warehouse product quantities and saves the changes to
	 * the database.
	 *
	 * @param warehouse The Warehouse object containing updated product quantities.
	 * @param modelMap  ModelMap for adding attributes to the view.
	 * @return The view name for the updated list of warehouses or an error page in
	 *         case of an exception.
	 */
	@PostMapping("/updateWarehouseQuantities")
	public String updateWarehouseQuantities(@ModelAttribute(Constants.WAREHOUSE) Warehouse warehouse,
			ModelMap modelMap) {
		try {
			// Update the warehouse quantities in the database
			Warehouse fetchedWarehouse = warehouseService.getWarehouseById(warehouse.getWareId());
			fetchedWarehouse.setProductQuantities(warehouse.getProductQuantities());
			warehouseService.saveWarehouse(fetchedWarehouse);

			// Log a success message when the update is completed
			LOGGER.info("Updated warehouse quantities for Warehouse ID: {}", fetchedWarehouse.getWareId());
		} catch (Exception e) {
			// Log an error message if an exception occurs during the update
			LOGGER.error("Error updating warehouse quantities: {}", e.getMessage());
		}

		List<Warehouse> warehousesList = warehouseService.getWarehousesList();
		modelMap.addAttribute(Constants.WAREHOUSES_LIST, warehousesList);
		String msg = "Warehouse updated with ID - " + warehouse.getWareId();
		modelMap.addAttribute(Constants.MSG, msg);

		// Redirect to the warehouse details page to display the updated details
		return Constants.WAREHOUSE_VIEW + Constants.WAREHOUSES_LIST;
	}

}
