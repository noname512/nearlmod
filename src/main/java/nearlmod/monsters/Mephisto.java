package nearlmod.monsters;

import com.megacrit.cardcrawl.actions.common.*;
import com.megacrit.cardcrawl.actions.unique.AddCardToDeckAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.curses.Parasite;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.MonsterStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.RegenPower;
import com.megacrit.cardcrawl.vfx.combat.DeckPoofEffect;
import nearlmod.actions.EndBattleAction;
import nearlmod.actions.ReduceMaxHpAction;
import nearlmod.actions.SummonFriendAction;
import nearlmod.orbs.Gummy;

import java.util.ArrayList;

public class Mephisto extends AbstractMonster {
    public static final String ID = "nearlmod:Mephisto";
    public static final MonsterStrings monsterStrings = CardCrawlGame.languagePack.getMonsterStrings(ID);
    public static final String NAME = monsterStrings.NAME;
    public static final String[] MOVES = monsterStrings.MOVES;
    public static final String[] DIALOG = monsterStrings.DIALOG;

    private final int heal_amt;
    private final int regen_amt;
    int turn = 0;
    private final int para_turn;

    public Mephisto(float x, float y) {
        super(NAME, ID, 120, 10.0F, 0, 170.0F, 320.0F, null, x, y);
        this.type = EnemyType.BOSS;
        if (AbstractDungeon.ascensionLevel >= 9)
            setHp(130);
        if (AbstractDungeon.ascensionLevel >= 19) {
            para_turn = 3;
        } else {
            para_turn = 5;
        }
        if (AbstractDungeon.ascensionLevel >= 4) {
            heal_amt = 10;
            regen_amt = 6;
        } else {
            heal_amt = 8;
            regen_amt = 4;
        }
        loadAnimation("resources/nearlmod/images/monsters/enemy_1507_mephi/enemy_1507_mephi33.atlas", "resources/nearlmod/images/monsters/enemy_1507_mephi/enemy_1507_mephi33.json", 1.5F);
        flipHorizontal = true;
        stateData.setMix("Idle", "Die", 0.1F);
        state.setAnimation(0, "Idle", true);
    }

    @Override
    public void usePreBattleAction() {
        addToBot(new SummonFriendAction(new Gummy()));
        if (AbstractDungeon.ascensionLevel < 15) {
            addToBot(new SummonFriendAction(new Gummy()));
        }
    }

    void gameOver() {
        AbstractDungeon.actionManager.cleanCardQueue();
        AbstractDungeon.effectList.add(new DeckPoofEffect(64.0F * Settings.scale, 64.0F * Settings.scale, true));
        AbstractDungeon.effectList.add(new DeckPoofEffect((float)Settings.WIDTH - 64.0F * Settings.scale, 64.0F * Settings.scale, false));
        AbstractDungeon.overlayMenu.hideCombatPanels();
        AbstractDungeon.getCurrRoom().smoked = true;
        addToBot(new AddCardToDeckAction(new Parasite()));
        addToBot(new AddCardToDeckAction(new Parasite()));
        addToBot(new AddCardToDeckAction(new Parasite()));
        addToBot(new EndBattleAction());
    }

    @Override
    public void takeTurn() {
        state.setAnimation(0, "Skill", false);
        state.addAnimation(0, "Idle", true, 0);
        if (nextMove == 1) {
            for (AbstractMonster m : AbstractDungeon.getCurrRoom().monsters.monsters) {
                if (!m.isDeadOrEscaped()) {
                    addToBot(new HealAction(m, this, heal_amt));
                    if (m != this) {
                        addToBot(new ApplyPowerAction(m, this, new RegenPower(m, regen_amt)));
                    }
                }
            }
        } else if (nextMove == 2) {
            // Summon
        } else {
            int parasiteCount = 1;
            for (AbstractCard c : AbstractDungeon.player.masterDeck.group) {
                if (c instanceof Parasite) {
                    parasiteCount ++;
                }
            }
            addToBot(new MakeTempCardInDiscardAndDeckAction(new Parasite()));
            addToBot(new ReduceMaxHpAction(AbstractDungeon.player, parasiteCount * 3));
        }
        rollMove();
    }

    @Override
    public void rollMove() {
        turn ++;
        ArrayList<Integer> possibleMoves = new ArrayList<>();
        int lastMoves = -1;
        if (turn >= 3) {
            if (moveHistory.get(moveHistory.size() - 1).equals(moveHistory.get(moveHistory.size() - 2))) {
                lastMoves = moveHistory.get(moveHistory.size()-1);
            }
        }
        if ((AbstractDungeon.ascensionLevel >= 15) && (turn >= para_turn) && (lastMoves != 3)) {
            possibleMoves.add(3);
        }
        if (lastMoves != 2) { // && 宿主数量不超过上限
            possibleMoves.add(2);
        }
        if (lastMoves != 1) {
            possibleMoves.add(1);
        }

        int move = possibleMoves.get(AbstractDungeon.aiRng.random(possibleMoves.size()));
        if (move == 1) {
            setMove((byte) 1, Intent.BUFF);
        }
        else if (move == 2) {
            setMove((byte) 2, Intent.UNKNOWN);
        }
        else if (move == 3) {
            setMove((byte) 3, Intent.STRONG_DEBUFF);
        }
    }

    @Override
    protected void getMove(int i) {
    }
}
