Agentic KT Documentation

	GL Code & Cost Code Prediction System for Non-PO Invoices using LLMs
Use case Overview:
The GL Code & Cost Code Prediction System is an LLM-powered system that automatically predicts GL Code, Cost Code, and GL Category for Non-PO invoice line items using LLM, Historical Invoice Data, and Azure AI Search.
The system reduces manual effort in Accounts Payable invoice coding by automatically tagging invoice lines with the correct finance codes.

Step 1: Receive Invoice Request
The system starts when the client or ERP system sends invoice data to the API. The invoice contains header details and line item descriptions that need GL Code and Cost Code prediction.
Input:
Invoice details including Invoice ID, Invoice Date, Vendor ID, Approver Name, and Invoice Line Items with Line Description.
Step 2: Load Configuration
The system loads the configuration file from storage. This configuration file contains field mappings, prompts for LLM, Azure AI Search index names, confidence score weights, and filtering rules.
Step 3: Fetch GL Master Categories
The system queries the search index to retrieve GL categories relevant to the vendor or company using Azure AI Search. These categories are master GL categories such as IT Services, Office Supplies, Travel, etc.

Step 4: LLM Stage 1 (GL Category Identification)
The first LLM analyzes the invoice line description and selects the most relevant GL categories from the list of available GL categories retrieved in the previous step.
Step 5: Historical Invoice Search using Azure AI search
The system searches historical invoice data using Azure AI search to find similar invoice line descriptions that were previously processed and already have GL Code and Cost Code assigned. This search uses similarity matching and filters like Vendor and GL Category.
Step 6: Confidence Score Calculation and Decision Logic
The system calculates a confidence score using similarity score, frequency of historical matches, and recency of historical transactions. Based on the confidence score, the system decides whether to directly assign GL Code and Cost Code or send the case to the second LLM for better matching.
Step 7: LLM Stage 2 (Best Historical Match Selection)
If multiple historical matches exist or confidence is low, the system sends the invoice line description and historical matched descriptions to the second LLM. The LLM selects the best matching historical invoice line.
Step 8: GL Code and Cost Code Prediction
The system assigns GL Code and Cost Code based on the selected historical match or the highest confidence historical record. Confidence scores are also generated for the prediction.
Output:
Final response containing GL Code, Cost Code, GL Category, and Confidence Score for each invoice line.

	GL Code & Cost Code Prediction System for Non-PO Invoices using AI-ML
1. Use Case Overview
The GL Code & Cost Code Prediction System is a machine learning–based solution that automatically predicts GL Codes and Cost Codes for Non-PO invoice line items using historical invoice data.
The system uses historical pattern matching and machine learning to assign the most appropriate codes and return confidence scores.
2. Input
The system receives invoice line item data through an API request.
Input Data Fields:
•	Vendor Name 
•	Invoice Description 
•	Company Code 
•	Cost Center 
•	Amount 
•	Invoice Date 
•	Other configurable fields 
Input is usually sent in JSON format from ERP or client system.

3. Output
The system returns predicted GL Code and Cost Code along with confidence score.
Output Fields
•	Predicted GL Code 
•	Predicted Cost Code 
•	Confidence Score 
•	Prediction Type (Exact Match / ML Prediction) 

4. Step-by-Step Working
Step 1: Request Received
The client or ERP system sends invoice line item data to the Azure Function API. The Azure Function acts as the entry point of the system.

Step 2: Load Configuration
The system loads configuration files from Azure Blob Storage.
These configuration files contain:
•	Field mappings 
•	Model parameters 
•	Training file location 
The input fields are mapped to the system fields based on configuration.

Step 3: Check Training Data
The system checks whether new training data is available in Blob Storage.
•	If new training data is available → Training pipeline runs 
•	If no new training data → Existing model is used 
Step 4 : Training Pipeline (If New Data Available)
Step 4.1: Load Training Data
The system loads historical invoice data from CSV/Excel file.
Step 4.2 : Data Cleaning
Data cleaning is performed:
•	Remove null values 
•	Remove duplicate records 
•	Format text fields 
•	Encode categorical variables 
Step 4.3: Feature Selection
The system identifies the best column combinations that help predict GL Code and Cost Code.
Example combinations:
•	Vendor + Description 
•	Vendor + Company 
•	Company + Cost Center 

Step 4.4: Create Scope1 Dataset
Records where the same input combination always maps to the same GL Code and Cost Code are stored in Scope1 dataset. These are exact match records.

Step 4.5: Create Scope2 Dataset
Records where the same input combination maps to different GL Codes or Cost Codes are stored in Scope2 dataset. These records are used for machine learning training.


Step 4.6: Train Machine Learning Model
The system trains an AdaBoost classifier using Scope2 dataset.
After training, the following files are saved in Blob Storage:
•	Trained model file 
•	Scope1 dataset 
•	Scope2 dataset 
•	Label encoders 

 Prediction / Inference Process
After training or if model already exists, prediction process starts.

Step 5: Load Model and Scope Files
The system loads:
•	Trained ML model 
•	Scope1 dataset 
•	Scope2 dataset 
•	Encoders 

Step 6: Scope1 Exact Match Check
For each invoice line item:
•	The system checks if the input combination exists in Scope1 dataset. 
If match found:
•	GL Code and Cost Code are returned 
•	Confidence = 100% 
•	Prediction completed 
If match not found:
•	System moves to Scope2 prediction 

Step 7: Scope2 Machine Learning Prediction
The system checks whether the input data exists in the Scope2 training distribution.
If exists:
•	ML model predicts GL Code and Cost Code 
•	Model probability is returned as confidence score 
If not exists:
•	System returns No Prediction 
•	Confidence = 0% 

Step 8: Generate Final Response
The system combines predictions for all invoice line items and generates final JSON response, which is returned to the client system.
Predictions → JSON Response → Client / ERP System





	Tax Code LLM

1. Use Case Overview
The Tax Code Identification System is an LLM-based solution that automatically recommends tax codes for invoice line items. It uses historical data, similarity search, and AI reasoning to improve accuracy, reduce manual effort, and maintain consistency across transactions.

2. Input and Output
2.1 Input
The system accepts structured invoice data:
•	Invoice Header: Invoice ID, Date, Company Code, Vendor Number, Country
•	Line Items: Invoice Line ID, Description, Tax Percentage
•	Tax Master Data: List of valid tax codes with descriptions and rates
•	Additional Inputs: Historical invoice data

2.2 Output
For each invoice line item, the system returns:
•	Invoice Line ID
•	Recommended Tax Code
•	Justification
•	Confidence Score


3. Step-by-Step Working

Step 1: Request Processing
The system receives invoice data through an API endpoint. Configuration settings are loaded to control processing logic.
Step 2: Data Transformation
Incoming data is transformed into a standardized internal format using predefined mappings. This ensures consistency across all processing stages regardless of source format.
Step 3: Historical Data Enrichment
For each invoice line item, the system retrieves relevant historical records:
1.	Convert the line description into a numerical vector (embedding)
2.	Apply filters such as company, vendor, and tax percentage
3.	Perform similarity search on historical data
4.	Retrieve the most relevant past records
5.	Remove duplicate entries
6.	Sort results based on recency
7.	Limit the number of records (top K)
8.	Attach historical data to the line item
This step provides contextual data for accurate decision-making.
Step 4: Parallel Processing
If multiple line items exist, the system splits them and processes each line independently in parallel. This improves performance and reduces overall processing time.

Step 5: Tax Code Identification
For each enriched line item:
1.	Validate input data
2.	Summarize historical records (grouped by tax code and frequency)
3.	Prepare input for AI model including:
o	Current description
o	Historical examples
o	Available tax codes
4.	Send data to AI model (LLM)
5.	Receive recommended tax code and justification
Step 6: Confidence Score Calculation
A confidence score is computed using multiple factors:
•	Similarity between current and historical descriptions
•	Matching parameters (vendor/company)
•	Frequency of tax code usage in history
•	Recency of historical records
These factors are combined using weighted scoring to produce a final confidence value.
Step 7: Response Generation
Results from all line items are merged into a single structured response and returned to the client system.



	Multi-Agent Invoice to PO Line Tagging

1. Use Case Overview
This system automates tagging of invoice line items to corresponding PO line items in Accounts Payable. It handles cases where invoices lack structured references by using rule-based matching and AI (RAG + LLM) for accurate and scalable matching.
2. Input and Output
2.1 Input
The system takes:
•	Invoice Line Items: ID, description, optional product number
•	PO Line Items: PO ID, description, product number
•	Configuration and environment settings
2.2 Output
The system returns:
•	InvoiceLineItemNumber → PurchaseOrderNumber mapping
•	MatchPercentage (confidence score)
•	MatchingReason (exact or semantic match)

3. Working Steps

Step 1: API Request
The client sends invoice and PO data to the system via an HTTP POST request. This triggers the Azure Function, which acts as the entry point.


Step 2: Supervisor Initialization
The Supervisor Agent initializes the system:
•	Loads configuration
•	Initializes Semantic Kernel
•	Registers all plugins
•	Creates Orchestrator and RAG agents
Step 3: Workflow Decision (Orchestrator Agent)
The Orchestrator Agent determines the execution flow based on configuration:
•	If rules are enabled → run Rule-Based Matching first
•	Otherwise → directly proceed to RAG-based matching
Step 4: Input Data Processing
The Input Data Handler Plugin:
•	Validates input structure
•	Cleans text (lowercase, remove special characters)
•	Converts data into structured format (DataFrames)
Step 5: Rule-Based Matching
The Rule-Based Plugin performs fast deterministic matching:
•	Exact Product/Service Number match
•	Substring match in descriptions
•	Only unique values considered for 1:1 matching

Output:
•	Matched pairs (exact matches)
•	Remaining unmatched invoice and PO lines

Step 6: RAG-Based Semantic Matching
The RAG Agent processes unmatched items using AI:
6.1 Embedding Generation
•	Convert invoice and PO descriptions into vector embeddings
6.2 Vector Storage
•	Store PO embeddings in an in-memory vector database
6.3 Hybrid Search
•	Retrieve Top-K similar PO lines for each invoice using:
o	Cosine similarity (semantic)
o	Keyword overlap
6.4 LLM Reasoning
•	LLM evaluates candidates and selects best match
•	Assigns match percentage and reasoning

Step 7: Confidence Scoring
Each match is scored using a weighted formula:
•	Semantic similarity score
•	LLM confidence
•	Cross-field validation (e.g., product number match)

Step 8: 1:1 Matching Enforcement
The system ensures strict one-to-one mapping:
•	Each invoice line maps to only one PO line
•	Duplicate PO assignments are removed
•	Lower confidence matches are detagged

Step 9: Retry Mechanism 
If enabled:
•	Unmatched invoices are reprocessed
•	Already used PO lines are excluded
•	Matching is retried with updated context

Step 10: External Validation (Optional)
Matched pairs can be validated against Salesforce ERP:
•	Confirms PO existence and status
•	Checks vendor consistency
•	Ensures quantity and pricing validity
Invalid matches are removed from final results.

Step 11: Output Formatting
Final results are returned as a structured JSON response.
	GRN Variance Analysis

1. Use Case Overview
The GRN Variance Analysis System is an LLM-powered solution that helps Accounts Payable teams evaluate differences between invoice quantity and GRN quantity. It is especially useful for perishable goods, where some variance is expected due to natural factors. The system uses historical data, statistical analysis, and LLM reasoning to provide consistent and data-driven payment decisions.

2. System Workflow (Step-by-Step)
Step 1: Request Intake
The system receives an HTTP request containing:
•	Invoice details: Invoice ID, Date, Vendor
•	Line item details: Product, Quantity, GRN Quantity, UOM
This request is handled by an Azure Function endpoint.
Step 2: Configuration Loading
The system loads a dynamic configuration file from cloud storage.
This configuration defines:
•	Field mappings
•	SQL query parameters
•	LLM prompts
•	Processing rules
Step 3: Historical Data Retrieval
The system queries a database to fetch historical records based on:
•	Vendor
•	Product
•	Unit of Measure (UOM)
•	Time range (typically last 2 years)
These records contain past invoice and GRN quantities, which are used for analysis.
Step 4: Historical Enrichment
Each invoice line item is enriched with relevant historical data.
This enables comparison between current and past transactions.
Step 5: Parallel Processing
If multiple line items exist:
•	The system splits them into individual units
•	Processes them concurrently using parallel execution
This improves performance and reduces latency.
Step 6: Input Validation
Each line item is validated to ensure:
•	Required fields are present
•	Data types are correct
•	Quantities are valid
Invalid inputs are rejected early to maintain system reliability.
Step 7: Variance Calculation
For each line item, variance is calculated using:
Variance (%) = |(GRN Quantity – Invoice Quantity) / GRN Quantity| × 100
This represents the percentage difference between received and billed quantities.
Step 8: Business Rule Filtering
Before performing advanced analysis, the system applies rules:
•	If Invoice Quantity < GRN Quantity → Skip (favorable case)
•	If quantities are negative → Skip (returns or adjustments)
•	If no historical data → Skip AI analysis
These rules optimize performance and ensure only relevant cases are analyzed.
Step 9: Statistical Analysis
The system computes statistical metrics from historical data:
•	Mean (average variance)
•	Minimum and maximum variance
•	Percentiles (P5, P25, P50, P75, P95)
•	Monthly trends (seasonality)
These metrics define what is considered “normal” behavior.
Step 10: AI-Based Decision Making
The system sends the following to an AI model:
•	Current variance
•	Product details
•	Historical statistical summary
The AI model evaluates:
•	Whether the variance is within acceptable limits
•	Whether it deviates significantly from historical patterns
It returns:
•	A recommendation (Accept / Investigate)
•	A detailed justification
Step 11: Output Validation
The AI response is validated to ensure:
•	Proper structure
•	Required fields are present
•	Data format is correct
Step 12: Trend Analysis
The system calculates variance trends over the past 12 months.
This helps identify patterns such as:
•	Increasing variance over time
•	Seasonal fluctuations
Step 13: Final Response Generation
The system compiles the final output containing:
•	Invoice and vendor details
•	Line item-wise variance
•	LLM-generated recommendation
•	Justification
•	Monthly trend data
This is returned as a structured JSON response.


