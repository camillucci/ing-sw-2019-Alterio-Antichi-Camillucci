# Documentazione Glith JavaFX

## Quando avviene:
A volte, all'avvio dell'applicazione, alcuni nodi di javafx vengono "sostituiti" con dei rettangoli bianchi.
Il comportamento del nodo non viene comunque intaccato dal problema (tutti i vari eventi, come il click ad esempio, funzionano).
In più, se si prova a ridimensionare la finestra, esiste sempre un punto in cui l'interfaccia torna alla normalità, e in cui anche ritornando alla grandezza originale, il glitch sembra essere sparito.
Queste sono alcune cose utili che potrebbero aiutare:
- Il problema non è i legato al controller o al model; infatti facendo partire la GUI senza "logica di gioco" il problema si verifica ugualmente.
- Il problema è difficilmente riproducibile. Avviando lo stage, senza interagire con la finestra in alcun modo, il problema può verificarsi in alcuni casi, e non verificarsi in altri
- A volte viene lanciata una NullPointerException, il cui stack è interamente nel codice interno alle classi di javafx
- L'eccezione di cui al punto 3 non sembra (almeno in maniera evidente) implicare il glitch. Succede che si verifichi l'uno senza l'altro e viceversa
- La gui non crasha in nessun caso e l'interfaccia è sempre reattiva come dovrebbe, Il problema è unicamente a livello grafico appunto, i quadrati bianchi si sovrappongono al nodo, e l'esperienza utente ne risente.
    Quindi nessun crash non deterministico, ma piuttosto ci sono dei glitch non deterministici. 
## Link relativi all'errore:
- ### Piazza:
    - https://piazza.com/class/jsmyge37pv7404?cid=234
- ### OpenJDK:
    - https://bugs.openjdk.java.net/browse/JDK-8205092
- ### StackOverflow:
    - https://stackoverflow.com/questions/37750553/javafx-graphic-glitch-white-boxes
    - https://stackoverflow.com/questions/49610596/javafx-nodes-whiting-out
    - https://stackoverflow.com/questions/35829998/possible-javafx-canvas-bug-when-rendering-lots-of-paths
    - https://stackoverflow.com/questions/48457765/javafx-sporadic-rendering-issue-disappearing-buttons-controls
    - https://stackoverflow.com/questions/38684068/javafx-ui-bug-when-loading-fxml-programatically
    - https://stackoverflow.com/questions/28033903/javafx-not-rendering-properly
    
## StackTrace che ogni tanto compare:
    it.polimi.ingsw.AdrenalineLauncherClient
    Exception in thread "JavaFX Application Thread" java.lang.NullPointerException
     at com.sun.javafx.scene.input.PickResultChooser.processOffer(PickResultChooser.java:185)
     at com.sun.javafx.scene.input.PickResultChooser.offer(PickResultChooser.java:143)
     at javafx.scene.Node.doComputeIntersects(Node.java:5263)
     at javafx.scene.Node$1.doComputeIntersects(Node.java:456)
     at com.sun.javafx.scene.NodeHelper.computeIntersectsImpl(NodeHelper.java:180)
     at com.sun.javafx.scene.NodeHelper.computeIntersects(NodeHelper.java:133)
     at javafx.scene.Node.intersects(Node.java:5234)
     at javafx.scene.Node.doPickNodeLocal(Node.java:5171)
     at javafx.scene.Node$1.doPickNodeLocal(Node.java:450)
     at com.sun.javafx.scene.NodeHelper.pickNodeLocalImpl(NodeHelper.java:175)
     at com.sun.javafx.scene.NodeHelper.pickNodeLocal(NodeHelper.java:128)
     at javafx.scene.Node.pickNode(Node.java:5203)
     at javafx.scene.Parent.pickChildrenNode(Parent.java:805)
     at javafx.scene.Parent$1.pickChildrenNode(Parent.java:136)
     at com.sun.javafx.scene.ParentHelper.pickChildrenNode(ParentHelper.java:113)
     at javafx.scene.layout.Region.doPickNodeLocal(Region.java:3160)
     at javafx.scene.layout.Region$1.doPickNodeLocal(Region.java:184)
     at com.sun.javafx.scene.layout.RegionHelper.pickNodeLocalImpl(RegionHelper.java:104)
     at com.sun.javafx.scene.NodeHelper.pickNodeLocal(NodeHelper.java:128)
     at javafx.scene.Node.pickNode(Node.java:5203)
     at javafx.scene.Parent.pickChildrenNode(Parent.java:805)
     at javafx.scene.Parent$1.pickChildrenNode(Parent.java:136)
     at com.sun.javafx.scene.ParentHelper.pickChildrenNode(ParentHelper.java:113)
     at javafx.scene.layout.Region.doPickNodeLocal(Region.java:3160)
     at javafx.scene.layout.Region$1.doPickNodeLocal(Region.java:184)
     at com.sun.javafx.scene.layout.RegionHelper.pickNodeLocalImpl(RegionHelper.java:104)
     at com.sun.javafx.scene.NodeHelper.pickNodeLocal(NodeHelper.java:128)
     at javafx.scene.Node.pickNode(Node.java:5203)
     at javafx.scene.Parent.pickChildrenNode(Parent.java:805)
     at javafx.scene.Parent$1.pickChildrenNode(Parent.java:136)
     at com.sun.javafx.scene.ParentHelper.pickChildrenNode(ParentHelper.java:113)
     at javafx.scene.layout.Region.doPickNodeLocal(Region.java:3160)
     at javafx.scene.layout.Region$1.doPickNodeLocal(Region.java:184)
     at com.sun.javafx.scene.layout.RegionHelper.pickNodeLocalImpl(RegionHelper.java:104)
     at com.sun.javafx.scene.NodeHelper.pickNodeLocal(NodeHelper.java:128)
     at javafx.scene.Node.pickNode(Node.java:5203)
     at javafx.scene.Parent.pickChildrenNode(Parent.java:805)
     at javafx.scene.Parent$1.pickChildrenNode(Parent.java:136)
     at com.sun.javafx.scene.ParentHelper.pickChildrenNode(ParentHelper.java:113)
     at javafx.scene.layout.Region.doPickNodeLocal(Region.java:3160)
     at javafx.scene.layout.Region$1.doPickNodeLocal(Region.java:184)
     at com.sun.javafx.scene.layout.RegionHelper.pickNodeLocalImpl(RegionHelper.java:104)
     at com.sun.javafx.scene.NodeHelper.pickNodeLocal(NodeHelper.java:128)
     at javafx.scene.Node.pickNode(Node.java:5203)
     at javafx.scene.Parent.pickChildrenNode(Parent.java:805)
     at javafx.scene.Parent$1.pickChildrenNode(Parent.java:136)
     at com.sun.javafx.scene.ParentHelper.pickChildrenNode(ParentHelper.java:113)
     at javafx.scene.layout.Region.doPickNodeLocal(Region.java:3160)
     at javafx.scene.layout.Region$1.doPickNodeLocal(Region.java:184)
     at com.sun.javafx.scene.layout.RegionHelper.pickNodeLocalImpl(RegionHelper.java:104)
     at com.sun.javafx.scene.NodeHelper.pickNodeLocal(NodeHelper.java:128)
     at javafx.scene.Node.pickNode(Node.java:5203)
     at javafx.scene.Parent.pickChildrenNode(Parent.java:805)
     at javafx.scene.Parent$1.pickChildrenNode(Parent.java:136)
     at com.sun.javafx.scene.ParentHelper.pickChildrenNode(ParentHelper.java:113)
     at javafx.scene.layout.Region.doPickNodeLocal(Region.java:3160)
     at javafx.scene.layout.Region$1.doPickNodeLocal(Region.java:184)
     at com.sun.javafx.scene.layout.RegionHelper.pickNodeLocalImpl(RegionHelper.java:104)
     at com.sun.javafx.scene.NodeHelper.pickNodeLocal(NodeHelper.java:128)
     at javafx.scene.Node.pickNode(Node.java:5203)
     at javafx.scene.Parent.pickChildrenNode(Parent.java:805)
     at javafx.scene.Parent$1.pickChildrenNode(Parent.java:136)
     at com.sun.javafx.scene.ParentHelper.pickChildrenNode(ParentHelper.java:113)
     at javafx.scene.layout.Region.doPickNodeLocal(Region.java:3160)
     at javafx.scene.layout.Region$1.doPickNodeLocal(Region.java:184)
     at com.sun.javafx.scene.layout.RegionHelper.pickNodeLocalImpl(RegionHelper.java:104)
     at com.sun.javafx.scene.NodeHelper.pickNodeLocal(NodeHelper.java:128)
     at javafx.scene.Node.pickNode(Node.java:5203)
     at javafx.scene.Parent.pickChildrenNode(Parent.java:805)
     at javafx.scene.Parent$1.pickChildrenNode(Parent.java:136)
     at com.sun.javafx.scene.ParentHelper.pickChildrenNode(ParentHelper.java:113)
     at javafx.scene.layout.Region.doPickNodeLocal(Region.java:3160)
     at javafx.scene.layout.Region$1.doPickNodeLocal(Region.java:184)
     at com.sun.javafx.scene.layout.RegionHelper.pickNodeLocalImpl(RegionHelper.java:104)
     at com.sun.javafx.scene.NodeHelper.pickNodeLocal(NodeHelper.java:128)
     at javafx.scene.Node.pickNode(Node.java:5203)
     at javafx.scene.Parent.pickChildrenNode(Parent.java:805)
     at javafx.scene.Parent$1.pickChildrenNode(Parent.java:136)
     at com.sun.javafx.scene.ParentHelper.pickChildrenNode(ParentHelper.java:113)
     at javafx.scene.layout.Region.doPickNodeLocal(Region.java:3160)
     at javafx.scene.layout.Region$1.doPickNodeLocal(Region.java:184)
     at com.sun.javafx.scene.layout.RegionHelper.pickNodeLocalImpl(RegionHelper.java:104)
     at com.sun.javafx.scene.NodeHelper.pickNodeLocal(NodeHelper.java:128)
     at javafx.scene.Node.pickNode(Node.java:5203)
     at javafx.scene.Scene$MouseHandler.pickNode(Scene.java:4005)
     at javafx.scene.Scene.pick(Scene.java:2029)
     at javafx.scene.Scene$MouseHandler.process(Scene.java:3815)
     at javafx.scene.Scene$MouseHandler$1.run(Scene.java:3622)
     at com.sun.javafx.application.PlatformImpl.lambda$runLater$10(PlatformImpl.java:428)
     at java.base/java.security.AccessController.doPrivileged(AccessController.java:389)
     at com.sun.javafx.application.PlatformImpl.lambda$runLater$11(PlatformImpl.java:427)
     at com.sun.glass.ui.InvokeLaterDispatcher$Future.run(InvokeLaterDispatcher.java:96)
     at com.sun.glass.ui.win.WinApplication._runLoop(Native Method)
     at com.sun.glass.ui.win.WinApplication.lambda$runLoop$3(WinApplication.java:174)
     at java.base/java.lang.Thread.run(Thread.java:835)
