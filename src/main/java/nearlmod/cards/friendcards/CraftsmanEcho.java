package nearlmod.cards.friendcards;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.animations.VFXAction;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.actions.watcher.ChooseOneAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.CardStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.WeakPower;
import com.megacrit.cardcrawl.vfx.combat.IntimidateEffect;
import nearlmod.cards.AllinOne;
import nearlmod.cards.ToAtkStance;
import nearlmod.cards.ToDefStance;
import nearlmod.patches.AbstractCardEnum;
import nearlmod.powers.LightPower;

import java.util.ArrayList;
import java.util.Iterator;

public class CraftsmanEcho extends AbstractFriendCard {
    public static final String ID = "nearlmod:CraftsmanEcho";
    public static final CardStrings cardStrings = CardCrawlGame.languagePack.getCardStrings(ID);
    public static final String NAME = cardStrings.NAME;
    public static final String DESCRIPTION = cardStrings.DESCRIPTION;
    public static final String UPGRADE_DESCRIPTION = cardStrings.UPGRADE_DESCRIPTION;
    public static final String IMG_PATH = "images/cards/craftsmanecho.png";
    private static final int COST = 2;
    private static final int EXTRA_INC = 3;
    private static final int UPGRADE_PLUS_COST = 1;
    public static final String BG_IMG = "images/512/bg_friend_test.png";

    public CraftsmanEcho() {
        super(ID, NAME, IMG_PATH, COST, DESCRIPTION,
                CardType.SKILL, AbstractCardEnum.NEARL_GOLD,
                CardRarity.SPECIAL, CardTarget.SELF, "nearlmod:Blemishine");
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        ArrayList<AbstractCard> choice = new ArrayList<>();
        choice.add(new ToAtkStance(EXTRA_INC));
        choice.add(new ToDefStance(EXTRA_INC));
        addToBot(new ChooseOneAction(choice));
        AbstractCard card = new AllinOne();
        card.upgrade();
        card.exhaust = true;
        card.rawDescription += " NL 消耗 。";
        card.initializeDescription();
        p.hand.addToHand(card);
    }

    @Override
    public AbstractCard makeCopy() {
        return new CraftsmanEcho();
    }

    @Override
    public void upgrade() {
        if (!upgraded) {
            upgradeName();
            upgradeBaseCost(UPGRADE_PLUS_COST);
        }
    }
}
