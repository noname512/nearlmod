package nearlmod.cards;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.DamageAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.CardStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.AbstractPower;
import nearlmod.patches.AbstractCardEnum;

public class LightArrowStrike extends AbstractNearlCard {
    public static final String ID = "nearlmod:LightArrowStrike";
    public static final CardStrings cardStrings = CardCrawlGame.languagePack.getCardStrings(ID);
    public static final String NAME = cardStrings.NAME;
    public static final String DESCRIPTION = cardStrings.DESCRIPTION;
    public static final String UPGRADE_DESCRIPTION = cardStrings.UPGRADE_DESCRIPTION;
    public static final String IMG_PATH = "images/cards/lightarrowstrike.png";
    private static final int COST = 1;
    private static final int UPGRADE_COST = 0;

    public LightArrowStrike() {
        super(ID, NAME, IMG_PATH, COST, DESCRIPTION,
                CardType.ATTACK, AbstractCardEnum.NEARL_GOLD,
                CardRarity.UNCOMMON, CardTarget.ENEMY);
        tags.add(CardTags.STRIKE);
    }

    @Override
    public boolean canPlay(AbstractCard card) {
        if (card.cardID.equals(ID) && AbstractDungeon.player.getPower("nearlmod:LightPower") == null) {
            return false;
        } else {
            return super.canPlay(card);
        }
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        AbstractPower power = AbstractDungeon.player.getPower("nearlmod:LightPower");
        if (power == null) return;
        int damage = power.amount;
        power = AbstractDungeon.player.getPower("Strength");
        if (power != null) damage += power.amount;
        AbstractDungeon.actionManager.addToBottom(new DamageAction(m, new DamageInfo(p, damage, damageTypeForTurn), AbstractGameAction.AttackEffect.SLASH_DIAGONAL));
    }

    @Override
    public AbstractCard makeCopy() {
        return new LightArrowStrike();
    }

    @Override
    public void upgrade() {
        if (!upgraded) {
            upgradeName();
            upgradeBaseCost(UPGRADE_COST);
        }
    }
}
