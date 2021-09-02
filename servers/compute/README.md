### Compute Server

Reads from two sms queua and performs either of the following

1. KDE accross a grid with a given set of data
2. Populate Grid with given set of data.
3. given a grid will reduce a grid.

Server can handle both the map and reduce side depeing
on what is needed. 

Uses two SMQ's to compute and reduce all jobs containe
needed data and grids in XML format

1. Quea is for populate and compute operations
2. Quea is for reduce operations from other quea 
temporary results are stored in database as needed
