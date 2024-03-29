# Prova Finale Ingegneria del Software 2019
## Gruppo AM30

- ###   10588830    Federico Alterio ([@fedeAlterio](https://github.com/fedeAlterio))<br>federico.alterio@mail.polimi.it
- ###   10590066    Clarence Antichi ([@ClarenceAntichi](https://github.com/ClarenceAntichi))<br>clarence.antichi@mail.polimi.it
- ###   10517171    Nicola Camillucci ([@Camillucci](https://github.com/Camillucci))<br>nicola.camillucci@mail.polimi.it

| Functionality | State |
|:-----------------------|:------------------------------------:|
| Basic rules | [![RED](https://placehold.it/15/f03c15/f03c15)](#) |
| Complete rules | [![GREEN](https://placehold.it/15/44bb44/44bb44)](#) |
| Socket | [![GREEN](https://placehold.it/15/44bb44/44bb44)](#) |
| RMI | [![GREEN](https://placehold.it/15/44bb44/44bb44)](#) |
| GUI | [![GREEN](https://placehold.it/15/44bb44/44bb44)](#) |
| CLI | [![GREEN](https://placehold.it/15/44bb44/44bb44)](#) |
| Multiple games | [![GREEN](https://placehold.it/15/44bb44/44bb44)](#) |
| Persistence | [![RED](https://placehold.it/15/f03c15/f03c15)](#) |
| Domination or Towers modes | [![RED](https://placehold.it/15/f03c15/f03c15)](#) |
| Terminator | [![RED](https://placehold.it/15/f03c15/f03c15)](#) |

<!--
[![RED](https://placehold.it/15/f03c15/f03c15)](#)
[![YELLOW](https://placehold.it/15/ffdd00/ffdd00)](#)
[![GREEN](https://placehold.it/15/44bb44/44bb44)](#)
-->

## How to Start the Server:
- The server's jar must be launched from the terminal;
- All parameters are optional, but if entered they must be in order.
- #### Parameters (in this order):
    - socketPort (Integer, default value: 9999)
    - rmiPort (Integer, default value: 1099)
    - loginTimer (Integer, default value: 30)
    - turnTimer (Integer, default value: 120)
- #### Example:
        java -jar .\AdrenalineLauncherServer.jar 9999 1099 30 120
    
## How to Start the Client:
- The client's jar can be launched by double clicking it;
- For changing some of the parameters, for using the cli and for non connecting to localhost, the jar must be launched from the terminal;
- All parameters are optional, but if entered they must be in order.
- #### Parameters (in this order):
    - viewType (String "cli" or "gui", default value: gui)
    - ipAddress (String, default value: 127.0.0.1)
    - socketPort (Integer, default value: 9999)
    - rmiPort (Integer, default value: 1099)
- #### Example:
        java -jar .\AdrenalineLauncherClient.jar gui 127.0.0.1 9999 1099
