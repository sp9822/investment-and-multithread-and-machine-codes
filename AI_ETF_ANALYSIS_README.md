# AI-Powered ETF Analysis Integration

This implementation adds AI-powered analysis capabilities to the ETF response system using ChatGPT (GPT-4).

## Features

- **AI-Powered Analysis**: Uses ChatGPT to analyze ETF data from NSE and BSE exchanges
- **Comprehensive Data Processing**: Includes market trends, performance metrics, and investment insights
- **Fallback Mechanism**: Gracefully handles API unavailability with default responses
- **Configurable**: Supports different OpenAI models and timeout settings

## Setup Instructions

### 1. OpenAI API Key Configuration

Set your OpenAI API key as an environment variable:

```bash
export OPENAI_API_KEY="your-openai-api-key-here"
```

Or add it to your `application.properties`:

```properties
openai.api.key=your-openai-api-key-here
```

### 2. Configuration Options

The following properties can be configured in `application.properties`:

```properties
# OpenAI API Configuration
openai.api.key=${OPENAI_API_KEY:}          # Your OpenAI API key
openai.api.model=gpt-5                     # Model to use (gpt-4, gpt-3.5-turbo, etc.)
openai.api.timeout=60                      # Request timeout in seconds
```

### 3. Usage

The AI analysis is automatically integrated into the `EtfResponseMapper.getInvestableEtfs()` method. When you call this method, it will:

1. Fetch ETF data from NSE and BSE
2. Send the data to ChatGPT for analysis
3. Return the AI-generated investment insights

### 4. API Response Structure

The AI analysis will be included in the `EtfResponse` object as the third parameter:

```java
EtfResponse response = etfResponseMapper.getInvestableEtfs(requestBean);
// response.getAiOpinion() contains the ChatGPT analysis
```

## Implementation Details

### AiAnalysisService

The `AiAnalysisService` class handles:
- OpenAI API integration
- Prompt construction with ETF data
- Error handling and fallback responses
- Logging and monitoring

### Prompt Engineering

The AI prompt includes:
- NSE and BSE ETF data summaries
- Top performing ETFs
- Market statistics (advances, declines, volume)
- Request for comprehensive analysis covering:
  - Market sentiment
  - Performance trends
  - Risk assessment
  - Investment recommendations
  - Market outlook

### Error Handling

- Graceful degradation when API key is missing
- Fallback responses for API failures
- Comprehensive logging for debugging

## Testing

Run the test suite to verify the implementation:

```bash
mvn test -Dtest=AiAnalysisServiceTest
```

## Dependencies

The implementation uses:
- `com.theokanning.openai-gpt3-java:service:0.18.2` - OpenAI API client
- Spring Boot configuration management
- Jackson for JSON processing

## Security Considerations

- API keys should be stored as environment variables
- Never commit API keys to version control
- Consider using Spring Cloud Config for production deployments
- Monitor API usage to avoid unexpected costs

## Cost Management

- GPT-4 has higher costs than GPT-3.5-turbo
- Consider implementing caching for repeated requests
- Monitor token usage through OpenAI dashboard
- Set appropriate timeout values to avoid hanging requests

