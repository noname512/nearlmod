package nearlmod.cards;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.megacrit.cardcrawl.actions.common.DamageAction;
import com.megacrit.cardcrawl.actions.common.MakeTempCardInHandAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.cards.status.Burn;
import com.megacrit.cardcrawl.cards.status.Dazed;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.CardStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import nearlmod.NLMOD;
import nearlmod.orbs.Shining;
import nearlmod.patches.AbstractCardEnum;

public class Sacrifice extends AbstractNearlCard {
    public static final String ID = "nearlmod:Sacrifice";
    public static final CardStrings cardStrings = CardCrawlGame.languagePack.getCardStrings(ID);
    public static final String NAME = cardStrings.NAME;
    public static final String DESCRIPTION = cardStrings.DESCRIPTION;
    public static final String UPGRADE_DESCRIPTION = cardStrings.UPGRADE_DESCRIPTION;
    public static final String IMG_PATH = "images/cards/sacrifice.png";
    private static final int COST = 0;
    private static final int ATTACK_DMG = 10;
    private static final int UPGRADE_PLUS_DMG = 3;

    public Sacrifice() {
        super(ID, NAME, IMG_PATH, COST, DESCRIPTION,
                CardType.ATTACK, AbstractCardEnum.NEARL_GOLD,
                CardRarity.COMMON, CardTarget.ENEMY);
        damage = baseDamage = ATTACK_DMG;
        cardsToPreview = new Burn();
    }

    @Override
    public boolean extraTriggered() {
        return NLMOD.checkOrb(Shining.ORB_ID);
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        setCardsToPreview();
        addToBot(new DamageAction(m, new DamageInfo(p, damage, damageTypeForTurn)));
        addToBot(new MakeTempCardInHandAction(cardsToPreview, 2));
    }

    private void setCardsToPreview() {
        if (AbstractDungeon.player != null) {
            if (extraTriggered()) cardsToPreview = new Dazed();
            else cardsToPreview = new Burn();
        }
    }

    @Override
    public void renderCardPreview(SpriteBatch sb) {
        setCardsToPreview();
        super.renderCardPreview(sb);
    }

    @Override
    public AbstractCard makeCopy() {
        return new Sacrifice();
    }

    @Override
    public void upgrade() {
        if (!upgraded) {
            upgradeName();
            upgradeDamage(UPGRADE_PLUS_DMG);
        }
    }
}
