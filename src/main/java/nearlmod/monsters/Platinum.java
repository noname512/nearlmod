package nearlmod.monsters;

import com.badlogic.gdx.graphics.Texture;
import com.esotericsoftware.spine.AnimationState;
import com.megacrit.cardcrawl.actions.animations.TalkAction;
import com.megacrit.cardcrawl.actions.common.DamageAction;
import com.megacrit.cardcrawl.actions.common.EscapeAction;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.MonsterStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;

public class Platinum extends AbstractMonster {
    public static final String ID = "nearlmod:Platinum";
    public static final MonsterStrings monsterStrings = CardCrawlGame.languagePack.getMonsterStrings(ID);
    public static final String NAME = monsterStrings.NAME;
    public static final String[] MOVES = monsterStrings.MOVES;
    public static final String[] DIALOG = monsterStrings.DIALOG;
    public static final String IMAGE = "images/monsters/platinum.png";

    public Platinum(float x, float y) {
        super(NAME, ID, 100, 0, 0, 150.0F, 320.0F, IMAGE, x, y);
        this.type = EnemyType.NORMAL;
        if (AbstractDungeon.ascensionLevel >= 7)
            setHp(110);
    }

    @Override
    public void usePreBattleAction() {
        addToBot(new TalkAction(this, DIALOG[0], 0.3F, 3.0F));
    }

    @Override
    public void takeTurn() {
        addToBot(new EscapeAction(this));
    }

    @Override
    protected void getMove(int i) {
        setMove((byte)99, Intent.ESCAPE);
    }
}