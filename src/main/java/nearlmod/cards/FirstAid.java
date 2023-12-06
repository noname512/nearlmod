package nearlmod.cards;

import com.evacipated.cardcrawl.mod.stslib.actions.tempHp.AddTemporaryHPAction;
import com.megacrit.cardcrawl.actions.common.GainBlockAction;
import com.megacrit.cardcrawl.actions.watcher.ChangeStanceAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.CardStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import nearlmod.patches.AbstractCardEnum;
import nearlmod.stances.DefStance;

public class FirstAid extends AbstractNearlCard {
    public static final String ID = "nearlmod:FirstAid";
    public static final CardStrings cardStrings = CardCrawlGame.languagePack.getCardStrings(ID);
    public static final String NAME = cardStrings.NAME;
    public static final String DESCRIPTION = cardStrings.DESCRIPTION;
    public static final String UPGRADE_DESCRIPTION = cardStrings.UPGRADE_DESCRIPTION;
    public static final String IMG_PATH = "images/cards/firstaid.png";
    private static final int COST = 1;
    private static final int BLOCK_AMT = 10;
    private static final int UPGRADE_PLUS_BLOCK = 4;

    public FirstAid() {
        super(ID, NAME, IMG_PATH, COST, DESCRIPTION,
                AbstractCard.CardType.SKILL, AbstractCardEnum.NEARL_GOLD,
                AbstractCard.CardRarity.RARE, AbstractCard.CardTarget.SELF);
        block = baseBlock = BLOCK_AMT;
    }

    @Override
    public boolean extraTriggered() {
        return AbstractDungeon.player.currentHealth * 2 <= AbstractDungeon.player.maxHealth;
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        if (!p.stance.ID.equals(DefStance.STANCE_ID)) {
            AbstractDungeon.actionManager.addToBottom(new ChangeStanceAction(new DefStance()));
        }
        applyPowers();
        AbstractDungeon.actionManager.addToBottom(new GainBlockAction(p, p, block));
        if (extraTriggered()) {
            AbstractDungeon.actionManager.addToBottom((new AddTemporaryHPAction(p, p, block)));
            if (p.stance.ID.equals(DefStance.STANCE_ID)) {
                p.state.setAnimation(0, "Skill", false);
                p.state.addAnimation(0, "Idle", true, 0);
            }
        }
    }

    @Override
    public void applyPowers() {
        if (!AbstractDungeon.player.stance.ID.equals(DefStance.STANCE_ID)) {
            baseBlock += DefStance.incNum;
            baseBlock += DefStance.defInc;
        }
        super.applyPowers();
        if (!AbstractDungeon.player.stance.ID.equals(DefStance.STANCE_ID)) {
            baseBlock -= DefStance.incNum;
            baseBlock -= DefStance.defInc;
            isBlockModified = (block != baseBlock);
        }
    }
    @Override
    public AbstractCard makeCopy() {
        return new FirstAid();
    }

    @Override
    public void calculateCardDamage(AbstractMonster mo) {
        if (!AbstractDungeon.player.stance.ID.equals(DefStance.STANCE_ID)) {
            baseBlock += DefStance.incNum;
            baseBlock += DefStance.defInc;
        }
        super.calculateCardDamage(mo);
        if (!AbstractDungeon.player.stance.ID.equals(DefStance.STANCE_ID)) {
            baseBlock -= DefStance.incNum;
            baseBlock -= DefStance.defInc;
            isBlockModified = (baseBlock != block);
        }
    }

    @Override
    public void upgrade() {
        if (!upgraded) {
            upgradeName();
            upgradeBlock(UPGRADE_PLUS_BLOCK);
        }
    }
}
