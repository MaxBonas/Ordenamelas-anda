package com.correos.delivery.repository;

import com.example.routes.Address;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * In-memory repository for storing {@link Address} instances during route creation.
 */
public class AddressRepository {

    private final List<Address> addresses = new ArrayList<>();

    /** Add a new address to the repository. */
    public void add(Address address) {
        addresses.add(address);
    }

    /**
     * Return an immutable view of all stored addresses.
     */
    public List<Address> getAll() {
        return Collections.unmodifiableList(addresses);
    }

    /** Remove all addresses from the repository. */
    public void clear() {
        addresses.clear();
    }
}
