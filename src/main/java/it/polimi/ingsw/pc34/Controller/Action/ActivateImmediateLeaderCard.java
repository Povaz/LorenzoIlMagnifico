package it.polimi.ingsw.pc34.Controller.Action;

import it.polimi.ingsw.pc34.Controller.Game;
import it.polimi.ingsw.pc34.Controller.PlayerState;
import it.polimi.ingsw.pc34.Model.*;
import it.polimi.ingsw.pc34.SocketRMICongiunction.ClientType;

import java.io.IOException;
import java.util.List;

/**
 * Created by trill on 12/06/2017.
 */
public class ActivateImmediateLeaderCard implements CommandPattern{
    private final Game game;
    private final Player player;
    private final Board board;
    private final List<ImmediateLeaderCard> immediateLeaderCardsPositionated;
    private final Counter newCounter;
    private ImmediateLeaderCard leaderCard;
    private FamilyMember toChange;
    private int newValueFamilyMember;

    public ActivateImmediateLeaderCard(Game game, Player player){
        this.game = game;
        this.player = player;
        this.board = game.getBoard();
        this.immediateLeaderCardsPositionated = player.getPlayerBoard().getImmediateLeaderCardsPositioned();
        this.newCounter = new Counter(player.getPlayerBoard().getCounter());
        player.getPlayerBoard().getModifier();
        this.toChange = null;
        this.newValueFamilyMember = 0;
    }

    public boolean canDoAction() throws IOException{
        if(immediateLeaderCardsPositionated.isEmpty()){
            if (player.getClientType().equals(ClientType.GUI)) {
                game.getGameController().sendMessageChatGUI(player, "You don't have any leader card placed!", true);
            }
            else {
                game.getGameController().sendMessageCLI(player, "You don't have any leader card placed! \nWhat action you want to do? 1-action 2-place Leader Card 3-activate Leader Card 4-exchange Leader Card 5-skip");
            }
            return false;
        }

        leaderCard = game.getGameController().askWhichImmediateCardActivate(immediateLeaderCardsPositionated, player, "A");

        if(leaderCard == null){
            return false;
        }

        if(leaderCard.isActivated()){
            return false;
        }

        earnReward();

        if(!canChangeFamilyMemberValue()){
            return false;
        }

        return true;
    }

    private void earnReward(){
        if(leaderCard.getReward() != null){
            newCounter.sum(leaderCard.getReward());
        }
    }

    private boolean canChangeFamilyMemberValue() throws IOException{
        if(!leaderCard.isChangeColoredFamilyMamberValue()){
            return true;
        }
        newValueFamilyMember = leaderCard.getNewValueColoredFamilyMember();
        FamilyColor familyColor = game.getGameController().chooseFamilyMemberColorNotNeutral(player);
        for(FamilyMember fM : player.getPlayerBoard().getFamilyMembers()){
            if(fM.getColor() == familyColor){
                toChange = fM;
            }
        }
        if(toChange == null){
            return false;
        }
        return true;
    }

    public void doAction() throws IOException{
        // aggiorna risorse giocatore
        player.getPlayerBoard().setCounter(newCounter);

        // segna la carta come attivata
        leaderCard.setActivated(true);

        // fai gli altri effetti
        doOtherEffect();

        // esegui azioni aggiuntive della carta
        doBonusActions();
    }

    private void doOtherEffect(){
        if(leaderCard.isChangeColoredFamilyMamberValue()){
            toChange.setValue(newValueFamilyMember);
        }
    }

    private void doBonusActions() throws IOException{
        List<FamilyMember> actions = leaderCard.getActions();
        if(actions != null){
            for(FamilyMember fM : actions){
                if (player.getClientType().equals(ClientType.GUI)) {
                    switch (player.getConnectionType()) {
                        case RMI:
                            game.getGameController().updateRequested(player.getUsername());
                            break;
                        case SOCKET:
                            //Fill
                            break;
                    }
                }
                else {
                    game.getGameController().sendMessageCLI(player, board.toString());
                    game.getGameController().sendMessageCLI(player, player.getPlayerBoard().toString());
                }
                fM.setPlayer(player);
                doBonusAction(fM);
            }
        }
    }

    private void doBonusAction(FamilyMember fM) throws IOException{
        ActionSpot actionSpot;
        do{
            String info = fM.getAction() + ": " + fM.getValue();
            System.out.println("AZIONE AGGIUNTIVA!!!");
            System.out.println(fM.getAction() + ":  " + fM.getValue());
            game.getGameController().ghost.set(true);
            player.putFirst_State(PlayerState.ACTION);
            player.putSecond_State(PlayerState.ACTION_INPUT);
            if (player.getClientType().equals(ClientType.GUI)) {
                switch (player.getConnectionType()) {
                    case RMI:
                        game.getGameController().getServerRMI().openNewWindow(player, "/bonusaction", info);
                        break;
                    case SOCKET:
                        game.getGameController().getServerHandler(player.getUsername()).openNewWindow("/bonusaction", "toSynchro");
                        break;
                }
            }
            actionSpot = game.getGameController().getViewActionSpot(player);
            if(actionSpot != null){
                fM.setServantUsed(new Reward(RewardType.SERVANT, game.getGameController().getHowManyServants(player)));
            }
        } while(actionSpot != null && !(game.placeFamilyMember(fM, actionSpot)));
    }
}
