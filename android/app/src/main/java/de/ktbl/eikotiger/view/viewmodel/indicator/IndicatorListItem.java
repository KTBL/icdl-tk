package de.ktbl.eikotiger.view.viewmodel.indicator;

import javax.inject.Inject;

import de.ktbl.android.sharedlibrary.annotation.mock.Mock;
import de.ktbl.android.sharedlibrary.navigation.NavigationCommand;
import de.ktbl.android.sharedlibrary.util.Strings;
import de.ktbl.android.sharedlibrary.view.adapter.Clickable;
import de.ktbl.android.sharedlibrary.view.viewmodel.AbstractBaseViewModel;
import de.ktbl.eikotiger.view.fragment.indicator.IndicatorInfoFragmentModes;
import de.ktbl.eikotiger.view.fragment.indicator.IndicatorListFragmentDirections;
import timber.log.Timber;

public class IndicatorListItem extends AbstractBaseViewModel implements Clickable {

    private static final String TAG = IndicatorListItem.class.getSimpleName();

    public static final int DEFAULT_DESCRIPTION_LENGTH = 100;

    public boolean isMock;
    @Mock()
    private String title;
    private Long id;
    private Long sectionId;
    protected boolean clickEnabled;
    @Mock(upperValueBound = 15)
    private String description;

    @Inject
    public IndicatorListItem() {
        this.clickEnabled = true;
    }

    public IndicatorListItem(String title, String description, Long id) {
        this();
        this.id = id;
        //this.sectionId = sectionId;
        this.title = title;
        this.description = description;

    }


    public IndicatorListItem(String title, String description, Long id,
                             int description_length) {
        this.title = title;
        // If the description has more than 40 characters we have to shorten it
        this.description = Strings.shorten(description, description_length, true);
        this.id = id;
        //this.sectionId = sectionId;
        this.enableClick(true);
    }


    @Override
    public void enableClick(boolean enabled) {
        this.clickEnabled = enabled;
    }

    //This provides the callback - sg
    public void showDetails() {
        if (!this.clickEnabled) {
            return;
        }
        if (id == null) {
            id = -1L;
        }
        if (sectionId == null) {
            sectionId = -1L;
        }
        IndicatorListFragmentDirections.ActionShowIndicatorInfo action =
                IndicatorListFragmentDirections.actionShowIndicatorInfo(id,
                                                                        IndicatorInfoFragmentModes
                                                                                .FULL_VIEW); //Change for testing -sg
        //action.setMock(true);  - uncomment for testing without db - sg
        Timber.tag(TAG)
              .d("Navigate to indicator info with id %d", id);
        this.navigationEventHandler.notifyLiveEvent(new NavigationCommand(action));
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }
}
