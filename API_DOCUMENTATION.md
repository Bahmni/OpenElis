# OpenELIS REST API Documentation

This document provides comprehensive information about the REST endpoints available in OpenELIS for managing pending tests and uploading test results.

## Base URL
When running with Docker Compose: `http://localhost:8052`

## Authentication
All REST endpoints require authentication. You need to provide login credentials via form parameters:
- `loginName`: Your OpenELIS username
- `password`: Your OpenELIS password

## Available REST Endpoints

### 1. Resource Retrieval API
**Endpoint Pattern:** `/rest/{resourceType}/{uuid}`
**Method:** GET
**Description:** Retrieves specific resources by UUID

#### Supported Resource Types:
- **patient**: Get patient details by UUID
- **accession**: Get accession/sample details by UUID

#### Examples:
```bash
# Get patient by UUID
GET /rest/patient/{patient-uuid}

# Get accession by UUID  
GET /rest/accession/{accession-uuid}
```

#### Response Format:
Returns JSON data for the requested resource.

### 2. Scheduled Tasks Monitoring
**Endpoint:** `/rest/scheduledTasks`
**Method:** GET
**Description:** Get status of all scheduled background tasks

#### Example:
```bash
GET /rest/scheduledTasks
```

#### Response Format:
```json
[
  {
    "started": true,
    "taskClass": "TaskClassName",
    "lastExecutionTime": "2023-01-01T10:00:00Z",
    "nextExecutionTime": null
  }
]
```

### 3. Test Result Upload API
**Endpoint:** `/DoUpload.do`
**Method:** POST
**Content-Type:** `multipart/form-data`
**Description:** Upload test results via CSV file

#### Required Form Parameters:
- `loginName`: Username for authentication
- `password`: Password for authentication
- `importType`: Type of import - either "sample" or "patient"
- `file`: CSV file containing test results or patient data

#### CSV Format for Test Results (importType="sample"):
The CSV should contain columns for:
- Sample ID/Accession Number
- Test Name
- Result Value
- Result Date
- Other test-specific fields

#### CSV Format for Patient Data (importType="patient"):
The CSV should contain columns for:
- Patient ID
- First Name
- Last Name
- Date of Birth
- Gender
- Other patient-specific fields

#### Example cURL Request:
```bash
curl -X POST http://localhost:8052/DoUpload.do \
  -F "loginName=your_username" \
  -F "password=your_password" \
  -F "importType=sample" \
  -F "file=@test_results.csv"
```

#### Response:
- Success: Redirects to `/Upload.do`
- Error: Returns validation error page

## Getting Pending Tests

While there isn't a direct REST endpoint specifically for "pending tests", you can retrieve pending test information through the **Order** data model which contains:

### Order Object Structure:
```json
{
  "accessionNumber": "string",
  "uuid": "string", 
  "orderId": "string",
  "stNumber": "string",
  "firstName": "string",
  "middleName": "string", 
  "lastName": "string",
  "source": "string",
  "pendingTestCount": 0,
  "pendingValidationCount": 0,
  "referredTestCount": 0,
  "totalTestCount": 0,
  "collectionDate": "2023-01-01T10:00:00Z",
  "enteredDate": "2023-01-01T10:00:00Z",
  "isPrinted": false,
  "isCompleted": false,
  "comments": "string",
  "sectionNames": "string",
  "sampleType": "string",
  "priority": "string"
}
```

### Key Fields for Pending Tests:
- `pendingTestCount`: Number of tests pending for this order
- `pendingValidationCount`: Number of tests pending validation
- `referredTestCount`: Number of tests referred out
- `totalTestCount`: Total number of tests for this order
- `isCompleted`: Whether all tests are completed

### Accessing Pending Tests:
To get pending test information, you would need to:

1. **Use the accession endpoint** to get order details:
   ```bash
   GET /rest/accession/{accession-uuid}
   ```

2. **Check the pending counts** in the returned Order object to determine which tests are pending.

## Implementation Notes

### Authentication Flow:
1. All REST endpoints extend `WebServiceAction` which handles authentication
2. Authentication is validated against the session or via login credentials
3. Unauthorized requests return HTTP 401

### Handler Pattern:
The REST API uses a handler pattern where:
- `ResourceRetrieveAction` routes requests to appropriate handlers
- `Handlers` class maintains a registry of available handlers
- Each handler (PatientHandler, AccessionHandler) processes specific resource types

### Data Access:
- `OrderListDAOImpl` provides methods to query orders by status
- Supports filtering by date (today, before today)
- Handles different analysis statuses (pending, completed, referred)

### File Upload Processing:
- Uses `FileImporter` for CSV processing
- `TestResultPersister` handles test result data
- `PatientPersister` handles patient data
- Files are written to disk before processing

## Error Handling

### Common HTTP Status Codes:
- `200 OK`: Successful request
- `400 Bad Request`: Invalid request or missing parameters
- `401 Unauthorized`: Authentication failed
- `404 Not Found`: Resource not found

### Error Response Format:
Errors typically return appropriate HTTP status codes. For form-based endpoints, validation errors redirect to error pages.

## Usage Examples

### Complete Workflow Example:

1. **Check scheduled tasks status:**
   ```bash
   curl "http://localhost:8052/rest/scheduledTasks" \
     -d "loginName=admin" \
     -d "password=adminADMIN!"
   ```

2. **Get accession details (including pending test counts):**
   ```bash
   curl "http://localhost:8052/rest/accession/some-uuid" \
     -d "loginName=admin" \
     -d "password=adminADMIN!"
   ```

3. **Upload test results:**
   ```bash
   curl -X POST "http://localhost:8052/DoUpload.do" \
     -F "loginName=admin" \
     -F "password=adminADMIN!" \
     -F "importType=sample" \
     -F "file=@results.csv"
   ```

## Database Schema Notes

The system uses several key database tables:
- `analysis`: Contains test analysis records with status information
- `sample`: Contains sample information
- `clinlims.sample_human`: Links samples to patients
- Various status tables for tracking test progress

## Security Considerations

1. **Always use HTTPS in production**
2. **Store credentials securely** - never hardcode in applications
3. **Validate file uploads** - ensure CSV files are properly formatted
4. **Monitor file upload sizes** to prevent abuse
5. **Use proper session management** for web-based access

---

This documentation covers the main REST endpoints available in OpenELIS for managing pending tests and uploading test results. The system uses a combination of REST endpoints and traditional form-based actions to provide comprehensive laboratory information management capabilities.
