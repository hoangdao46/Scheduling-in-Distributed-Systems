public class week9 {
    /* 
    First-fit 
    Pros: It is extremely fast as the first job just claims the first available memory with space that is equal to it's size.
    Cons: It wastes a lot of memory, as it doesn't search for appropriate partition rather just the nearest. This waste of memory can make many jobs to not get enough space in the memory and would have to wait for a job to finish.
    External Fragmentation 

    Best-fit
    Pros: It is much more memory efficient than First-fit. As best-fit allocates the minimum available space. This works great when allocations of jobs are of small size
    Cons: Compared to First-fit, it is a much slower process. As it checks the whole memory for each job to find the closest-fitting size.
    External Fragmentation
    If allocations of jobs are of large size, this can lead to producing a lot of small fragments in memory 
    Slow deallocation as well as allocation. 

    Worst-fit
    Pros: Internal Fragmentation means a lot of small jobs can be allocated to one large partition.
    Cons: Since Worst-fit resource capacity is determined the same way as Best-fit, it is just as slow. 
    Since it finds largest first this can cause problems down the line as larger jobs might not be allocated. 

    */







}
