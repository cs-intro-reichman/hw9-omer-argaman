/**
 * Represents a managed memory space. The memory space manages a list of allocated 
 * memory blocks, and a list free memory blocks. The methods "malloc" and "free" are 
 * used, respectively, for creating new blocks and recycling existing blocks.
 */
public class MemorySpace {
	
	// A list of the memory blocks that are presently allocated
	private LinkedList allocatedList;

	// A list of memory blocks that are presently free
	private LinkedList freeList;

	/**
	 * Constructs a new managed memory space of a given maximal size.
	 * 
	 * @param maxSize
	 *            the size of the memory space to be managed
	 */
	public MemorySpace(int maxSize) {
		// initiallizes an empty list of allocated blocks.
		allocatedList = new LinkedList();
	    // Initializes a free list containing a single block which represents
	    // the entire memory. The base address of this single initial block is
	    // zero, and its length is the given memory size.
		freeList = new LinkedList();
		freeList.addLast(new MemoryBlock(0, maxSize));
	}

	/**
	 * Allocates a memory block of a requested length (in words). Returns the
	 * base address of the allocated block, or -1 if unable to allocate.
	 * 
	 * This implementation scans the freeList, looking for the first free memory block 
	 * whose length equals at least the given length. If such a block is found, the method 
	 * performs the following operations:
	 * 
	 * (1) A new memory block is constructed. The base address of the new block is set to
	 * the base address of the found free block. The length of the new block is set to the value 
	 * of the method's length parameter.
	 * 
	 * (2) The new memory block is appended to the end of the allocatedList.
	 * 
	 * (3) The base address and the length of the found free block are updated, to reflect the allocation.
	 * For example, suppose that the requested block length is 17, and suppose that the base
	 * address and length of the the found free block are 250 and 20, respectively.
	 * In such a case, the base address and length of of the allocated block
	 * are set to 250 and 17, respectively, and the base address and length
	 * of the found free block are set to 267 and 3, respectively.
	 * 
	 * (4) The new memory block is returned.
	 * 
	 * If the length of the found block is exactly the same as the requested length, 
	 * then the found block is removed from the freeList and appended to the allocatedList.
	 * 
	 * @param length
	 *        the length (in words) of the memory block that has to be allocated
	 * @return the base address of the allocated block, or -1 if unable to allocate
	 */
	public int malloc(int length) {	
		if (length <= 0) {
			return -1; // Invalid allocation request
		}
	
		// Iterate through freeList to find a block with sufficient length
		for (int i = 0; i < freeList.getSize(); i++) {
			MemoryBlock freeBlock = freeList.getBlock(i);
	
			if (freeBlock.length >= length) {
				int allocatedBaseAddress = freeBlock.baseAddress;
	
				// If the free block size matches exactly, remove it
				if (freeBlock.length == length) {
					freeList.remove(i);
				} else {
					// Otherwise, adjust the free block to reflect the allocated memory
					freeBlock.baseAddress += length;
					freeBlock.length -= length;
				}
	
				// Create a new allocated block and add it to allocatedList
				MemoryBlock allocatedBlock = new MemoryBlock(allocatedBaseAddress, length);
				allocatedList.addLast(allocatedBlock);
	
				return allocatedBaseAddress; // Return the base address of the allocated block
			}
		}
	
		return -1; // Return -1 if no suitable block is found
	}
		

	/**
	 * Frees the memory block whose base address equals the given address.
	 * This implementation deletes the block whose base address equals the given 
	 * address from the allocatedList, and adds it at the end of the free list. 
	 * 
	 * @param baseAddress
	 *            the starting address of the block to freeList
	 */
/**
 * Frees the memory block whose base address equals the given address.
 * This implementation deletes the block whose base address equals the given 
 * address from the allocatedList, and adds it to the free list. 
 * 
 * @param address
 *        the starting address of the block to be freed
 */
/**
 * Frees the memory block whose base address equals the given address.
 * 
 * @param address the starting address of the block to be freed
 * @throws IllegalArgumentException if the address is invalid or cannot be freed
 */
public void free(int address) {
		Node current = allocatedList.getFirst();
	
		if (allocatedList.getSize() == 0) {
			throw new IllegalArgumentException("index must be between 0 and size");
		}
	
		while (current.block.baseAddress != address && current != allocatedList.getLast()) {
			current = current.next;
		}
	
		if (current.block.baseAddress == address) {
			allocatedList.remove(current.block);
			freeList.addLast(current.block);
		}
	}

	/* 
		Node current = allocatedList.getFirst();
		if(allocatedList.getSize() == 0){
				throw new IllegalArgumentException(
						"index must be between 0 and size");
			}
		else{
			while(current.block.baseAddress != address && current != allocatedList.getLast()) {
				current = current.next;
			}
			if (current.block.baseAddress == address) {
				allocatedList.remove(current.block);
			freeList.addLast(current.block);
			}
		}
	}
		*/
	/* 
    if (allocatedList.getSize() == 0) {
        throw new IllegalArgumentException("index must be between 0 and size");
    }

    for (int i = 0; i < allocatedList.getSize(); i++) {
        MemoryBlock allocatedBlock = allocatedList.getBlock(i);

        if (allocatedBlock.baseAddress == address) {
            // Remove from allocatedList
            allocatedList.remove(i);

            // Check if the block is already in freeList
            for (int j = 0; j < freeList.getSize(); j++) {
                MemoryBlock freeBlock = freeList.getBlock(j);
                if (freeBlock.baseAddress == allocatedBlock.baseAddress) {
                    // Block is already in freeList; no need to add it again
                    return;
                }
            }

            // Add the freed block to the freeList in sorted order
            for (int j = 0; j < freeList.getSize(); j++) {
                MemoryBlock freeBlock = freeList.getBlock(j);

                if (allocatedBlock.baseAddress < freeBlock.baseAddress) {
                    freeList.add(j, allocatedBlock);
                    return;
                }
            }

            // If no suitable position was found, add to the end
            freeList.addLast(allocatedBlock);
            return;
        }
    }

    throw new IllegalArgumentException("index must be between 0 and size");
}
	
	*/
	/**
	 * A textual representation of the free list and the allocated list of this memory space, 
	 * for debugging purposes.
	 */
	public String toString() {
		return freeList.toString() + "\n" + allocatedList.toString();		
	}
	
	/**
	 * Performs defragmantation of this memory space.
	 * Normally, called by malloc, when it fails to find a memory block of the requested size.
	 * In this implementation Malloc does not call defrag.
	 */
	public void defrag() {
		for (int i = 0; i < freeList.getSize() - 1; i++) {
			for (int j = 0; j < freeList.getSize() - i - 1; j++) {
				MemoryBlock block1 = freeList.getBlock(j);
				MemoryBlock block2 = freeList.getBlock(j + 1);
	
				if (block1.baseAddress > block2.baseAddress) {
					// Swap the blocks
					freeList.remove(j + 1);
					freeList.add(j, block2);
				}
			}
		}
	
		// Merge adjacent blocks
		for (int i = 0; i < freeList.getSize() - 1; ) {
			MemoryBlock current = freeList.getBlock(i);
			MemoryBlock next = freeList.getBlock(i + 1);
	
			if (current.baseAddress + current.length == next.baseAddress) {
				// Merge the blocks
				current.length += next.length;
				freeList.remove(i + 1);
			} else {
				i++; // Only increment if no merge occurred
			}
		}
	}
}
