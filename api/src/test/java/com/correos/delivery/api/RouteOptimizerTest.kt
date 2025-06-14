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
    fun optimizeReturnsStopsOnSuccess() {
        val stops = listOf(Address("1","A","1",0.0,0.0))

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
