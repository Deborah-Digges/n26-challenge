# N26 Challenge

We​ ​would​ ​like​ ​to​ ​have​ ​a​ ​restful​ ​API​ ​for​ ​our​ ​statistics.​ ​The​ ​main​ ​use​ ​case​ ​for​ ​our​ ​API​ ​is​ ​to calculate​ ​realtime​ ​statistic​ ​from​ ​the​ ​last​ ​60​ ​seconds.​ ​There​ ​will​ ​be​ ​two​ ​APIs,​ ​one​ ​of​ ​them​ ​is called​ ​every​ ​time​ ​a​ ​transaction​ ​is​ ​made.​ ​It​ ​is​ ​also​ ​the​ ​sole​ ​input​ ​of​ ​this​ ​rest​ ​API.​ ​The​ ​other​ ​one returns​ ​the​ ​statistic​ ​based​ ​of​ ​the​ ​transactions​ ​of​ ​the​ ​last​ ​60​ ​seconds.

## Specs

1. POST​ ​/transactions
Every​ ​Time​ ​a​ ​new​ ​transaction​ ​happened,​ ​this​ ​endpoint​ ​will​ ​be​ ​called.
```
{
    ​​​​​​​​​​​​​"amount":​ ​12.3,
​ ​​ ​​ ​"timestamp":​ ​1478192204000 
}
```
- amount​​​​- ​​​is​​ a​ ​double ​​specifying ​​the ​​amount
- timestamp​​​-​​​transaction​​ time​ ​in ​​epoch ​​in ​​millis​ ​in ​​UTC ​​time​​zone​

Returns:​ ​Empty​ ​body​ ​with​ ​either​ ​201​ ​or​ ​204.
- 201​ ​-​ ​in​ ​case​ ​of​ ​success
- 204​ ​-​ ​if​ ​transaction​ ​is​ ​older​ ​than​ ​60​ ​seconds

2. GET /statistics
This​ ​is​ ​the​ ​main​ ​endpoint​ ​of​ ​this​ ​task,​ ​this​ ​endpoint​ ​have​ ​to​ ​execute​ ​in​ ​constant​ ​time​ ​and memory​ ​(O(1)).​ ​It​ ​returns​ ​the​ ​statistic​ ​based​ ​on​ ​the​ ​transactions​ ​which​ ​happened​ ​in​ ​the​ ​last​ ​60 seconds.
 
```
{
    ​"sum":​ ​1000,
    ​"avg":​ ​100, 
    "max":​ ​200, 
    "min":​ ​50, 
    "count":​ ​10
}
 ​​ ​​ ​​ ​​ ​​ ​​ ​​ ​​ ​​ ​​ ​​ ​​ ​​ ​​ ​​ ​​ ​​ ​​ ​​ ​​ ​​ ​​ ​​ ​​ ​​

```
Returns:

- sum: ​​​​is ​​a ​​double ​​specifying​ ​the ​​total ​​sum​ ​of ​​transaction​​ value​ ​in ​​the ​​last ​​60 ​​seconds 
- avg: ​​​​is ​​a ​​double ​​specifying​​ the​​ average​​ amount​​ of​​ transaction ​​value ​​in ​​the ​​last ​​60 seconds
- max​​​: is​​ a​​ double​​ specifying​​ single​​ highest​​ transaction​​ value ​​in​​ the ​​last ​​60​​ seconds
- min​​​​is​​: ​​double​​ specifying ​​single​​ lowest ​​transaction​​ value​​ in ​​the ​​last ​​60​​ seconds
- count​​​​: ​​a​​ long​​ specifying​​ the​​ total ​​number​​ of​​ transactions​​ happened​​ in​​ the​​ last ​​60 seconds

## Requirements
For​​ the​​ rest​​ api,​​the ​​biggest​ ​and​​ maybe ​​hardest ​​requirement ​​is​​ to ​​make ​​the ​​​​GET​ ​/statistics execute​ ​in​ ​constant​ ​time​ ​and​ ​space.​ ​The​ ​best​ ​solution​ ​would​ ​be​ ​O(1).​ ​It​ ​is​ ​very​ ​recommended​ ​to tackle​ ​the​ ​O(1)​ ​requirement​ ​as​ ​the​ ​last​ ​thing​ ​to​ ​do​ ​as​ ​it​ ​is​ ​not​ ​the​ ​only​ ​thing​ ​which​ ​will​ ​be​ ​rated​ ​in the​ ​code​ ​challenge.
Other​ ​requirements,​ ​which​ ​are​ ​obvious,​ ​but​ ​also​ ​listed​ ​here​ ​explicitly:
- The​ ​API​ ​have​ ​to​ ​be​ ​threadsafe​ ​with​ ​concurrent​ ​requests
- The​ ​API​ ​have​ ​to​ ​function​ ​properly,​ ​with​ ​proper​ ​result
- The​ ​project​ ​should​ ​be​ ​buildable,​ ​and​ ​tests​ ​should​ ​also​ ​complete​ ​successfully.​ ​e.g.​ ​If maven​ ​is​ ​used,​ ​then​ ​mvn​ ​clean​ ​install​ ​should​ ​complete​ ​successfully.
- The API should be able to deal with time discrepancy, which means, at any point of time, we​ ​could​ ​receive​ ​a​ ​transaction​ ​which​ ​have​ ​a​ ​timestamp​ ​of​ ​the​ ​past
- Make​ ​sure​ ​to​ ​send​ ​the​ ​case​ ​in​ ​memory​ ​solution​ ​without​ ​database​ ​(including​ ​in-memory
database)
- Endpoints​ ​have​ ​to​ ​execute​ ​in​ ​constant​ ​time​ ​and​ ​memory​ ​(O(1))
- Please​ ​complete​ ​the​ ​challenge​ ​using​ ​Java

## Solution

To implement the O(1) requirement of GET /statistics, an async task computes statistics in the background every 1ms. The solution is implemented as follows:

1. All new transactions coming in from the POST /transactions call are added to a list `unprocessedTransactions`
2. Every 1ms these transactions are 
    1. Consumed from the list by the background task
    2. Added to another list `tranactionsInWindow` which represent the transactions in the current window
    3. Statistics are computed and stored in-memory
3. A call to GET /statistics reads from the statistics in-memory and therefore executes in constant time

## Data structures used to ensure concurrency

For the 2 lists `unprocessedTransactions` and `transactionsInWindow` a synchronized list is used which ensures that concurrent updates to the list are managed correctly. However, the data-structure does not ensure that reads from the list are similarly managed. Hence, while reading unprocessed transactions, we need to explicitly synchronize on the `unprocessedTransactions` list.

## Testing

1. Unit tests are written in `JUnit`
2. Concurrency tests are written using the `thread-weaver` dependency
