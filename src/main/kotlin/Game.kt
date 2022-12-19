class Game() {


    var cards: MutableList<Card> = mutableListOf()
    var player: Player
    var player_tmp: ArtificialPlayer = ArtificialPlayer("cpu")
    var discardPile: MutableList<Card> = mutableListOf()
    var roundCount: Int = 0
    var next: String = " "
    var scoreBoard: ScoreBoard


    init {
        cards = createDeck()
        println()
        println("To play this game - please enter your name: ")
        println()
        player = Player(readln())
        println("${player.name}, you will be playing against the computer. Best of luck!")
        println()
        println()
        scoreBoard = ScoreBoard("Skyjo", player.name, player_tmp.name)
    }


    // creates and returns a deck of 150 cards, containing 10 cards of each value [-2,-1,0,1,2,3,4,5,6,7,8,9,10,11,12]
    fun createDeck(): MutableList<Card>{
        var i: Int = -2
        while(i <= 12) {
            for (x in 1..10) {
                cards.add(Card(i))
            }
            i++
        }
        return cards
    }


    // game is restricted to 1 round for presentational purposes
    fun startGame(){
        do{
            newRound()
        } while (roundCount <=0)
        finishGame()
    }


    // commands to be repeated each round:
    // roundCount goes 1 up
    // collect all cards & shuffle them
    // deal 12 cards to each player
    // flip one card open to the discard pile
    // each player flips tewo cards
    // calculate who is starting
    fun newRound(){
        roundCount++
        shuffleDeck()
        player.currentCards = dealCards()
        player_tmp.currentCards = dealCards()
        discardPile.add(cards.removeFirst())
        firstFlip()
        whoStartsRound()
        nextTurn()
        showScoreBoard()
    }


    // 12 cards are moved from the deck 'cards' to then be passed to create a playing field
    fun dealCards(): PlayingField {
        var dealtCards: MutableList<Card> = mutableListOf()
        for(x in 1..12){
            dealtCards.add(cards.removeFirst())
        }
        return PlayingField(dealtCards)
    }


    // each round starts with the players turn over any 2 cards in their playing field
    fun firstFlip(){
        println("To start the round each player is requested to turn over two cards in their playing field.")
        println()
        player.currentCards.display()
        println()
        player.flipCard(player.chooseCard())
        player.currentCards.display()
        println("One more.")
        player.flipCard(player.chooseCard())
        player.currentCards.display()
        println()
        println()
        println("The cpu is flipping over two cards now.")
        player_tmp.flipCard(player_tmp.chooseCard())
        player_tmp.flipCard(player_tmp.chooseCard())
        println()
        println("Both players have flipped two cards on their respective playing field.")
        println()
    }


    // TODO: why is there such a big gab between the print-lines ?
    fun whoStartsRound(){
        println("Whoever is currently having more points to count is starting this round.")
        if(player.currentCards.calculateScore()<player_tmp.currentCards.calculateScore()){
            next = player_tmp.name
            println("It is the ${next} that starts this round.")
        } else if (player.currentCards.calculateScore()>player_tmp.currentCards.calculateScore()) {
            next = player.name
            println("... ${next}, you ll start this round. It is your turn.")
        } else {
            println("The current score of the players is equal. Therefore we need to toss a coin.")
            next = listOf(player.name, player_tmp.name).random()
            println("${next} is starting this round.")
        }
    }


    fun nextTurn(){
        do {
            if (next == player.name) {
                takeTurn(player)
            } else if (next == player_tmp.name) {
                takeTurn(player_tmp)
            }
            next = switchTurn()
        } while(!(player.terminate || player_tmp.terminate))
        println()
        lastTurn()
    }


    // draw, decide, dismiss etc.
    fun takeTurn(player: Player){
        displayCurrentGame()
        player.keepOrDismiss(player.drawCard(discardPile,cards), discardPile)
        player.wantToCloseRound(discardPile)
    }


    // alternates who's turn it is
    fun switchTurn(): String {
        if(next == player.name)
            return player_tmp.name
        else
            return player.name
    }


    fun displayCurrentGame(){
        println()
        println("><><><><><><><><><><><><><><><><><><><><><><><><><><><")
        println("Playing field of CPU: ")
        player_tmp.currentCards.display()
        println("><><><><><><><><><><><><><><><><><><><><><><><><><><><")
        println()
        println("             DRAW-PILE".padEnd(35, ' ') + "DISCARD-PILE")
        println("                 X".padEnd(40, ' ') + discardPile.last().front.toString())
        println()
        println("><><><><><><><><><><><><><><><><><><><><><><><><><><><")
        println("Playing field of ${player.name}: ")
        player.currentCards.display()
        println("><><><><><><><><><><><><><><><><><><><><><><><><><><><")

    }


    fun lastTurn(){
        displayCurrentGame()
        if (next == player.name) {
            player.keepOrDismiss(player.drawCard(discardPile,cards), discardPile)
        } else if (next == player_tmp.name) {
            player_tmp.keepOrDismiss(player_tmp.drawCard(discardPile, cards), discardPile)
        }
        player_tmp.endOfTurnCheck(discardPile)
        if(player_tmp.currentCards.allCardsRevealed == false){
            player_tmp.currentCards.flipRest()
            player_tmp.currentCards.checkAllColumns(discardPile)
        }
    }


    // in case the drawpile is (almost) empty or at beginning of new round
    fun shuffleDeck(){
        if(cards.size != 0) {
            while (!cards.isEmpty()) {
                discardPile.add(cards.removeFirst())
            }
        }
        discardPile.shuffle()
        while (!discardPile.isEmpty()) {
            cards.add(discardPile.removeFirst())
        }
        discardPile.clear()
        discardPile.add(cards.removeFirst())
    }


    fun showScoreBoard(){
        scoreBoard.extendBoard(roundCount, player.currentCards.calculateScore(), player_tmp.currentCards.calculateScore())
        scoreBoard.display()
    }


    fun finishGame(){
        scoreBoard.victoryCeremony()
    }

}