package nearlmod.cards;

import com.megacrit.cardcrawl.actions.common.GainBlockAction;
import com.megacrit.cardcrawl.actions.watcher.ChangeStanceAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.CardStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import nearlmod.actions.UseLightAction;
import nearlmod.patches.AbstractCardEnum;
import nearlmod.patches.NearlTags;
import nearlmod.stances.DefStance;

public class GumboBreadBowl extends AbstractNearlCard {
    public static final String ID = "nearlmod:GumboBreadBowl";
    public static final CardStrings cardStrings = CardCrawlGame.languagePack.getCardStrings(ID);
    public static final String NAME = cardStrings.NAME;
    public static final String DESCRIPTION = cardStrings.DESCRIPTION;
    public static final String UPGRADE_DESCRIPTION = cardStrings.UPGRADE_DESCRIPTION;
    public static final String IMG_PATH = "images/cards/gumbobreadbowl.png";
    private static final int COST = 0;
    private static final int BLOCK_AMT = 2;
    private static final int UPGRADE_PLUS_BLOCK = 2;

    public GumboBreadBowl() {
        super(ID, NAME, IMG_PATH, COST, DESCRIPTION,
                CardType.SKILL, AbstractCardEnum.NEARL_GOLD,
                CardRarity.COMMON, CardTarget.SELF);
        block = baseBlock = BLOCK_AMT;
        tags.add(NearlTags.IS_USE_LIGHT_AFTER);
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        if (!p.stance.ID.equals(DefStance.STANCE_ID)) {
            addToBot(new ChangeStanceAction(new DefStance()));
        }
        addToBot(new GainBlockAction(p, p, block));
        addToBot(new UseLightAction(p));
    }

    private void preUpd() {
        if (!AbstractDungeon.player.stance.ID.equals(DefStance.STANCE_ID)) {
            baseBlock += DefStance.incNum;
            baseBlock += DefStance.defInc;
        }
    }

    private void postUpd() {
        if (!AbstractDungeon.player.stance.ID.equals(DefStance.STANCE_ID)) {
            baseBlock -= DefStance.incNum;
            baseBlock -= DefStance.defInc;
            isBlockModified = (block != baseBlock);
        }
    }

    @Override
    public void applyPowers() {
        preUpd();
        super.applyPowers();
        postUpd();
    }

    @Override
    public AbstractCard makeCopy() {
        return new GumboBreadBowl();
    }

    @Override
    public void calculateCardDamage(AbstractMonster mo) {
        preUpd();
        super.calculateCardDamage(mo);
        postUpd();
    }

    @Override
    public void upgrade() {
        if (!upgraded) {
            upgradeName();
            upgradeBlock(UPGRADE_PLUS_BLOCK);
        }
    }
}
