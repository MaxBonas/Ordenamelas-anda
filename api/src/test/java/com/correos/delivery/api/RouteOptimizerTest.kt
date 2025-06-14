import com.correos.delivery.api.RouteOptimizer
import com.example.routes.Address
import okhttp3.*
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test
import org.mockito.Mockito.*
import java.io.IOException

class RouteOptimizerTest {
    private lateinit var client: OkHttpClient
    private lateinit var optimizer: RouteOptimizer

    @Before
    fun setUp() {
        client = mock(OkHttpClient::class.java)
        optimizer = RouteOptimizer(client)
    }

    @Test
    fun optimizeReordersStopsOnSuccess() {
        val stops = listOf(
            Address("1", "A", "1", 0.0, 0.0),
            Address("2", "B", "2", 1.0, 1.0),
            Address("3", "C", "3", 2.0, 2.0),
            Address("4", "D", "4", 3.0, 3.0)
        )

        val call = mock(Call::class.java)
        val json = "{" +
                "\"routes\":[{" +
                "\"optimizedIntermediateWaypointIndex\":[1,0]" +
                "}]" +
                "}"
        val responseBody = ResponseBody.create(MediaType.parse("application/json"), json)
        val response = Response.Builder()
            .code(200)
            .protocol(Protocol.HTTP_1_1)
            .message("OK")
            .request(Request.Builder().url("https://routes.googleapis.com").build())
            .body(responseBody)
            .build()

        `when`(client.newCall(any())).thenReturn(call)
        `when`(call.execute()).thenReturn(response)

        val result = optimizer.optimize(stops)

        val expected = listOf(stops[0], stops[2], stops[1], stops[3])
        assertEquals(expected, result)
    }

    @Test
    fun optimizeReturnsOriginalOnInvalidResponse() {
        val stops = listOf(
            Address("1", "A", "1", 0.0, 0.0),
            Address("2", "B", "2", 1.0, 1.0)
        )

        val call = mock(Call::class.java)
        val responseBody = ResponseBody.create(MediaType.parse("application/json"), "{}")
        val response = Response.Builder()
            .code(200)
            .protocol(Protocol.HTTP_1_1)
            .message("OK")
            .request(Request.Builder().url("https://routes.googleapis.com").build())
            .body(responseBody)
            .build()

        `when`(client.newCall(any())).thenReturn(call)
        `when`(call.execute()).thenReturn(response)

        val result = optimizer.optimize(stops)
        assertEquals(stops, result)
    }

    @Test
    fun optimizeReturnsStopsOnFailure() {
        val stops = listOf(Address("1","A","1",0.0,0.0))

        val call = mock(Call::class.java)
        `when`(client.newCall(any())).thenReturn(call)
        `when`(call.execute()).thenThrow(IOException("boom"))

        val result = optimizer.optimize(stops)
        assertEquals(stops, result)
    }
}
