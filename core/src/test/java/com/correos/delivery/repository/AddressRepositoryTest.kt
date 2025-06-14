import com.correos.delivery.repository.AddressRepository
import com.correos.delivery.core.model.Address
import org.junit.Assert.*
import org.junit.Before
import org.junit.Test

class AddressRepositoryTest {
    private lateinit var repository: AddressRepository

    @Before
    fun setUp() {
        repository = AddressRepository()
    }

    @Test
    fun addAndGetAll() {
        val address = Address("28080", "Street", "1", 1.0, 2.0)
        repository.add(address)
        val all = repository.getAll()
        assertEquals(1, all.size)
        assertEquals(address, all[0])
    }

    @Test
    fun clearRemovesAllAddresses() {
        repository.add(Address("11111", "A", "1", 0.0, 0.0))
        repository.add(Address("22222", "B", "2", 0.0, 0.0))
        repository.clear()
        assertTrue(repository.getAll().isEmpty())
    }
}
