package nearlmod.powers;

import basemod.BaseMod;
import basemod.interfaces.CloneablePowerInterface;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.megacrit.cardcrawl.actions.common.MakeTempCardInHandAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.helpers.ImageMaster;
import com.megacrit.cardcrawl.localization.PowerStrings;
import com.megacrit.cardcrawl.powers.AbstractPower;
import nearlmod.cards.friendcards.FocusedBombardment;

public class BombardmentStudiesPower extends AbstractPower implements CloneablePowerInterface {
    public static final String POWER_ID = "nearlmod:BombardmentStudiesPower";
    public static final PowerStrings powerStrings = CardCrawlGame.languagePack.getPowerStrings(POWER_ID);
    public static final String NAME = powerStrings.NAME;
    public static final String[] DESCRIPTIONS = powerStrings.DESCRIPTIONS;
    public int bombCnt;
    public int bombPlusCnt;

    public BombardmentStudiesPower(AbstractCreature owner, int amount, int bombCnt, int bombPlusCnt) {
        name = NAME;
        ID = POWER_ID;
        this.owner = owner;
        region128 = new TextureAtlas.AtlasRegion(ImageMaster.loadImage("resources/nearlmod/images/powers/bombardmentstudies power 128.png"), 0, 0, 128, 128);
        region48 = new TextureAtlas.AtlasRegion(ImageMaster.loadImage("resources/nearlmod/images/powers/bombardmentstudies power 48.png"), 0, 0, 48, 48);
        type = PowerType.BUFF;
        this.amount = amount;
        this.bombCnt = bombCnt;
        this.bombPlusCnt = bombPlusCnt;
        updateDescription();
    }

    @Override
    public void updateDescription() {
        description = DESCRIPTIONS[0] + amount + DESCRIPTIONS[1];
        if (bombCnt > 0) {
            if (bombPlusCnt > 0) {
                description += DESCRIPTIONS[2] + bombCnt + DESCRIPTIONS[3] + DESCRIPTIONS[5] + bombPlusCnt + DESCRIPTIONS[4] + DESCRIPTIONS[6];
            } else {
                description += DESCRIPTIONS[2] + bombCnt + DESCRIPTIONS[3] + DESCRIPTIONS[6];
            }
        } else {
            if (bombPlusCnt > 0) {
                description += DESCRIPTIONS[2] + bombPlusCnt + DESCRIPTIONS[4] + DESCRIPTIONS[6];
            }
        }
    }

    @Override
    public void atStartOfTurn() {
        AbstractCard card = new FocusedBombardment();
        if (bombCnt > 0) {
            BaseMod.MAX_HAND_SIZE += bombCnt;
            addToBot(new MakeTempCardInHandAction(card, bombCnt));
            bombCnt = 0;
        }
        card = card.makeCopy();
        card.upgrade();
        if (bombPlusCnt > 0) {
            BaseMod.MAX_HAND_SIZE += bombPlusCnt;
            addToBot(new MakeTempCardInHandAction(card, bombPlusCnt));
            bombPlusCnt = 0;
        }
        updateDescription();
    }

    @Override
    public AbstractPower makeCopy() {
        return new BombardmentStudiesPower(owner, amount, bombCnt, bombPlusCnt);
    }
}
