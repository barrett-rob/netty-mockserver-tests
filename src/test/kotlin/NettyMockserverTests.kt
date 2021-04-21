import io.kotlintest.shouldBe
import org.apache.http.client.methods.RequestBuilder
import org.apache.http.impl.client.HttpClientBuilder
import org.junit.jupiter.api.AfterAll
import org.junit.jupiter.api.BeforeAll
import org.junit.jupiter.api.Test
import org.mockserver.integration.ClientAndServer
import org.mockserver.model.HttpRequest
import org.mockserver.model.HttpResponse

class NettyMockserverTests {

    @Test
    fun `should succeed with GET abc from mock server`() {
        mockServer.`when`(HttpRequest.request().withMethod("GET").withPath("/abc"))
            .respond(HttpResponse.response().withStatusCode(200).withBody("OK"))

        val httpClient = HttpClientBuilder.create().build()
        httpClient.use {
            val httpResponse = httpClient.execute(RequestBuilder.get("http://localhost:18081/abc").build())
            httpResponse.statusLine.statusCode shouldBe 200
        }
        mockServer.reset()
    }

    @Test
    fun `should succeed with each GET abc from mock server`() {

        (0..99).forEach { i ->
            val path = "/abc/$i"
            println("testing GET path: $path")
            mockServer.`when`(HttpRequest.request().withMethod("GET").withPath(path))
                .respond(HttpResponse.response().withStatusCode(200).withBody("OK"))
            val httpClient = HttpClientBuilder.create().build()
            httpClient.use {
                val httpResponse = httpClient.execute(RequestBuilder.get("http://localhost:18081$path").build())
                httpResponse.statusLine.statusCode shouldBe 200
            }
            mockServer.reset()
        }
    }

    @Test
    fun `should succeed with each GET xyz from mock server`() {

        (0..99).forEach { i ->
            val path = "/xyz/$i"
            println("testing GET path: $path")
            mockServer.`when`(HttpRequest.request().withMethod("GET").withPath(path))
                .respond(HttpResponse.response().withStatusCode(200).withBody("OK"))
            val httpClient = HttpClientBuilder.create().build()
            httpClient.use {
                val httpResponse = httpClient.execute(RequestBuilder.get("http://localhost:18081$path").build())
                httpResponse.statusLine.statusCode shouldBe 200
            }
            mockServer.reset()
        }
    }

    companion object {

        private lateinit var mockServer: ClientAndServer

        @JvmStatic
        @BeforeAll
        fun beforeClass() {
            mockServer = ClientAndServer.startClientAndServer(18081)
        }

        @JvmStatic
        @AfterAll
        fun afterClass() {
            mockServer.stop() // may take up to 10 seconds to stop successfully before wait times out
        }

    }

}