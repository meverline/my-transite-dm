## Compute Manager

Manages the computation of user requested KDE this 
involves the following:

1. Read request sent from Heatmap web gui
2. Query database for KDE parameter data
3. Break (if nesscary) a single KDE grid into multiple
grids for  computer server to work on
4. Notify user when computation is done.
5. Restart KDE compute if problems

Manager works by using SMQ for read jobs that
are to be process. Jobs are sent via the HeatMap Gui
